package cz.muni.fi.pv168.cashflowevidence.wiring;

import cz.muni.fi.pv168.cashflowevidence.data.storage.dao.CategoryDao;
import cz.muni.fi.pv168.cashflowevidence.data.storage.dao.RepeatingTransactionDao;
import cz.muni.fi.pv168.cashflowevidence.data.storage.dao.TransactionDao;
import cz.muni.fi.pv168.cashflowevidence.data.storage.db.DatabaseManager;
import cz.muni.fi.pv168.cashflowevidence.data.storage.mapper.CategoryMapper;
import cz.muni.fi.pv168.cashflowevidence.data.storage.mapper.RepeatingTransactionMapper;
import cz.muni.fi.pv168.cashflowevidence.data.storage.mapper.TransactionMapper;
import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.CategoryRepository;
import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.RepeatingTransactionRepository;
import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.Repository;
import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.TransactionRepository;
import cz.muni.fi.pv168.cashflowevidence.data.validation.CategoryValidator;
import cz.muni.fi.pv168.cashflowevidence.data.validation.RepeatingTransactionValidator;
import cz.muni.fi.pv168.cashflowevidence.data.validation.TransactionValidator;
import cz.muni.fi.pv168.cashflowevidence.data.validation.Validator;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.RepeatingTransaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;

public abstract class CommonDependencyProvider implements DependencyProvider {

    private final DatabaseManager databaseManager;

    private final Repository<Category> categories;
    private final Repository<Transaction> transactions;
    private final Repository<RepeatingTransaction> repeatingTransactions;
    private final Validator<Category> categoryValidator;
    private final Validator<Transaction> transactionValidator;
    private final Validator<RepeatingTransaction> repeatingTransactionValidator;


    protected CommonDependencyProvider(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        this.categoryValidator = new CategoryValidator();
        this.transactionValidator = new TransactionValidator();
        this.repeatingTransactionValidator = new RepeatingTransactionValidator();

        this.categories = new CategoryRepository(
                new CategoryDao(databaseManager::getConnectionHandler),
                new CategoryMapper(categoryValidator));

        this.transactions = new TransactionRepository(
                new TransactionDao(databaseManager::getConnectionHandler),
                new TransactionMapper(categories::findByID, transactionValidator));

        this.repeatingTransactions = new RepeatingTransactionRepository(
                new RepeatingTransactionDao(databaseManager::getConnectionHandler),
                new RepeatingTransactionMapper(categories::findByID, repeatingTransactionValidator));
    }

    @Override
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Override
    public Repository<Category> getCategoryRepository() {
        return categories;
    }
    @Override
    public Repository<Transaction> getTransactionRepository() {
        return transactions;
    }
    @Override
    public Repository<RepeatingTransaction> getRepeatingTransactionRepository() {
        return repeatingTransactions;
    }
    @Override
    public Validator<Category> getCategoryValidator() {
        return categoryValidator;
    }
    @Override
    public Validator<Transaction> getTransactionValidator() {
        return transactionValidator;
    }
    @Override
    public Validator<RepeatingTransaction> getRepeatingTransactionValidator() {
        return repeatingTransactionValidator;
    }
}
