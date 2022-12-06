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
import cz.muni.fi.pv168.cashflowevidence.data.validation.Validator;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;

public class DaoImporter implements Importer {

    private final DaoSupplier<TransactionEntity> transactionEntityDaoSupplier;
    private final DaoSupplier<CategoryEntity> categoryEntityDaoSupplier;
    private final Supplier<TransactionHandler> transactionHandlerSupplier;
    private final Validator<Transaction> transactionValidator;
    private final Validator<Category> categoryValidator;
    private final FormatMapping<BatchImporter> importers;


    public DaoImporter(
            DaoSupplier<TransactionEntity> transactionEntityDaoSupplier,
            DaoSupplier<CategoryEntity> categoryEntityDaoSupplier,
            Supplier<TransactionHandler> transactionHandlerSupplier,
            Validator<Transaction> transactionValidator,
            Validator<Category> categoryValidator,
            Collection<BatchImporter> importers) {

        this.transactionEntityDaoSupplier = transactionEntityDaoSupplier;
        this.categoryEntityDaoSupplier = categoryEntityDaoSupplier;
        this.transactionHandlerSupplier = transactionHandlerSupplier;
        this.transactionValidator = transactionValidator;
        this.categoryValidator = categoryValidator;
        this.importers = new FormatMapping<>(importers);
    }

    @Override
    public void importData(String filePath) {
        var batch = getImporter(filePath).importBatch(filePath);

        try (var tx = transactionHandlerSupplier.get()) {
            DatabaseAccessObject<TransactionEntity> transactionDao = transactionEntityDaoSupplier.get(tx::connection);
            DatabaseAccessObject<CategoryEntity> categoryDao =categoryEntityDaoSupplier.get(tx::connection);

            transactionDao.deleteAll();
            categoryDao.deleteAll();

            var categoryMapper = new CategoryMapper(categoryValidator);
            batch.getCategories().forEach(c -> {
                categoryValidator.validate(c).intoException();
                var entity = categoryDao.create(categoryMapper.mapToEntity(c));
                c.setId(entity.id());
            });

            var transactionMapper = new TransactionMapper(id -> Optional.empty(), transactionValidator);
            batch.getTransactions().stream()
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
        if (importer == null)
            throw new BatchOperationException("Extension %s has no registered formatter".formatted(extension));
        return importer;
    }
}
