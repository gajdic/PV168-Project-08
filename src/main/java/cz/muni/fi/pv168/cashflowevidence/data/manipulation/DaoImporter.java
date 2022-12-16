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
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;

import javax.swing.*;
import java.util.ArrayList;
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
        var batch = getImporter(filePath).importBatch(filePath);

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
                    null, "Something went wrong while importing your transactions.");
            throw new BatchOperationException("Extension %s has no registered formatter".formatted(extension));
        }

        return importer;
    }

    private List<Transaction> getTransactionsToImport(Collection<Transaction> importedTransactions) {
        List<Transaction> duplicates = findDuplicate(importedTransactions);

        if (duplicates.size() != 0) {
            var sb = new StringBuilder("You are trying to import multiple duplicate transactions: ");
            var idx = 0;
            while (idx < 5 && idx != duplicates.size()) {
                sb.append(duplicates.get(idx).getName()).append(", ");
                idx += 1;
            }
            sb.deleteCharAt(sb.toString().length() - 2);

            var remainingDuplicates = duplicates.size() - idx;
            if (remainingDuplicates > 0) {
                sb.append(String.format("and %d more ", remainingDuplicates));
            }
            sb.append("?");

            int option = confirmDuplicateImports(sb.toString());
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
        }
        return importedTransactions.stream().toList();
    }

    private List<Transaction> findDuplicate(Collection<Transaction> importedTransactions) {
        List<Transaction> storedTransactions = transactionRepository.findAll();
        return  storedTransactions.stream()
                .filter(st -> importedTransactions.stream().anyMatch(it -> Objects.equals(st, it)))
                .toList();
    }

    private int confirmDuplicateImports(String message) {
        Object[] options = {"Import all","Import only non duplicate", "Do not import any"};

        return  JOptionPane.showOptionDialog(
                null,
                message,
                "Warning",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);
    }
}
