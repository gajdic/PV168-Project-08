package cz.muni.fi.pv168.cashflowevidence.data.manipulation;


import cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted.BatchImporter;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted.Format;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted.FormatMapping;
import cz.muni.fi.pv168.cashflowevidence.data.storage.dao.DaoSupplier;
import cz.muni.fi.pv168.cashflowevidence.data.storage.dao.DatabaseAccessObject;
import cz.muni.fi.pv168.cashflowevidence.data.storage.db.TransactionHandler;
import cz.muni.fi.pv168.cashflowevidence.data.storage.entity.CategoryEntity;
import cz.muni.fi.pv168.cashflowevidence.data.storage.entity.TransactionEntity;
import cz.muni.fi.pv168.cashflowevidence.data.storage.mapper.CategoryMapper;
import cz.muni.fi.pv168.cashflowevidence.data.storage.mapper.TransactionMapper;
import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.Repository;
import cz.muni.fi.pv168.cashflowevidence.data.validation.Validator;
import cz.muni.fi.pv168.cashflowevidence.exceptions.InvalidImportException;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;

import javax.swing.*;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DaoImporter implements Importer {

    private final DaoSupplier<TransactionEntity> transactionEntityDaoSupplier;
    private final DaoSupplier<CategoryEntity> categoryEntityDaoSupplier;
    private final Supplier<TransactionHandler> transactionHandlerSupplier;
    private final Validator<Transaction> transactionValidator;
    private final Validator<Category> categoryValidator;
    private final Repository<Transaction> transactionRepository;
    private final FormatMapping<BatchImporter> importers;


    public DaoImporter(
            DaoSupplier<TransactionEntity> transactionEntityDaoSupplier,
            DaoSupplier<CategoryEntity> categoryEntityDaoSupplier,
            Supplier<TransactionHandler> transactionHandlerSupplier,
            Validator<Transaction> transactionValidator,
            Validator<Category> categoryValidator,
            Repository<Transaction> transactionRepository,
            Collection<BatchImporter> importers) {

        this.transactionEntityDaoSupplier = transactionEntityDaoSupplier;
        this.categoryEntityDaoSupplier = categoryEntityDaoSupplier;
        this.transactionHandlerSupplier = transactionHandlerSupplier;
        this.transactionValidator = transactionValidator;
        this.categoryValidator = categoryValidator;
        this.transactionRepository = transactionRepository;
        this.importers = new FormatMapping<>(importers);
    }

    @Override
    public void importData(String filePath) {
        Batch batch;
        try {
            batch = getImporter(filePath).importBatch(filePath);
        }
        catch (InvalidImportException ex) {
            JOptionPane.showMessageDialog(
                    null, String.format("Import failed. %s", ex.getMessage()));
            return;
        }

        try (var tx = transactionHandlerSupplier.get()) {
            DatabaseAccessObject<TransactionEntity> transactionDao = transactionEntityDaoSupplier.get(tx::connection);
            DatabaseAccessObject<CategoryEntity> categoryDao =categoryEntityDaoSupplier.get(tx::connection);

            var categoryMapper = new CategoryMapper(categoryValidator);
            batch.getCategories().forEach(c -> {
                categoryValidator.validate(c).intoException();
                var entity = categoryDao.create(categoryMapper.mapToEntity(c));
                c.setId(entity.id());
            });

            var transactionMapper = new TransactionMapper(id -> Optional.empty(), transactionValidator);
            var transactions = getTransactionsToImport(batch.getTransactions());
            transactions.stream()
                    .peek(t -> transactionValidator.validate(t).intoException())
                    .map(transactionMapper::mapToEntity)
                    .forEach(transactionDao::create);

            tx.commit();
        }
    }

    @Override
    public Collection<Format> getFormats() {
        return importers.getFormats();
    }

    private BatchImporter getImporter(String filePath) {
        var extension = filePath.substring(filePath.lastIndexOf('.') + 1);
        var importer = importers.findByExtension(extension);
        if (importer == null) {
            JOptionPane.showMessageDialog(
                    null, "Import failed. Invalid file format.");
            throw new BatchOperationException("Extension %s has no registered formatter".formatted(extension));
        }

        return importer;
    }

    private List<Transaction> getTransactionsToImport(Collection<Transaction> importedTransactions) {
        List<Transaction> duplicates = findDuplicate(importedTransactions);

        if (duplicates.size() != 0) {
            var sb = new StringBuilder("You are trying to import multiple duplicate transactions: ");
            if (duplicates.size() == 1) {
                sb = new StringBuilder("You are trying to import duplicate transaction ");
            }
            var idx = 0;
            while (idx < 5 && idx != duplicates.size()) {
                sb.append(duplicates.get(idx).getName()).append(", ");
                idx += 1;
            }
            sb.delete(sb.toString().length() - 2, sb.toString().length());

            var remainingDuplicates = duplicates.size() - idx;
            if (remainingDuplicates > 0) {
                sb.append(String.format("and %d more", remainingDuplicates));
            }
            sb.append(". \nHow do you want to proceed?");
            if (duplicates.size() == 1) {
                confirmOneDuplicateImport(sb.toString(), importedTransactions);
            } else {
                importedTransactions = confirmMultipleDuplicateImports(sb.toString(), importedTransactions, duplicates);
            }
        }
        return importedTransactions.stream().toList();
    }

    private List<Transaction> findDuplicate(Collection<Transaction> importedTransactions) {
        List<Transaction> storedTransactions = transactionRepository.findAll();
        return  importedTransactions.stream()
                .filter(it -> storedTransactions.stream().anyMatch(st -> Objects.equals(st, it)))
                .toList();
    }

    private Collection<Transaction> confirmMultipleDuplicateImports(String message,
                                                            Collection<Transaction> importedTransactions,
                                                            List<Transaction> duplicates) {
        Object[] options = {"Import all","Import only non duplicate", "Do not import any"};

        int option = JOptionPane.showOptionDialog(
                null,
                message,
                "Warning",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);

        switch (option) {
            case 0:
                break;
            case 1:
                importedTransactions = importedTransactions
                        .stream()
                        .filter(t -> !duplicates.contains(t))
                        .collect(Collectors.toList());
                break;
            case 2:
                importedTransactions.clear();
                break;
        }
        return importedTransactions;
    }
    private void confirmOneDuplicateImport(String message,
                                           Collection<Transaction> importedTransactions) {
        Object[] options = {"Import", "Do not import"};

        int option = JOptionPane.showOptionDialog(
                null,
                message,
                "Warning",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                null);

        switch (option) {
            case 0:
                break;
            case 1:
                importedTransactions.clear();
                break;
        }
    }
}
