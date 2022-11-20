package cz.muni.fi.pv168.cashflowevidence.wiring;

import cz.muni.fi.pv168.cashflowevidence.data.storage.db.DatabaseManager;
import cz.muni.fi.pv168.cashflowevidence.data.validation.CategoryValidator;
import cz.muni.fi.pv168.cashflowevidence.data.validation.RepeatingTransactionValidator;
import cz.muni.fi.pv168.cashflowevidence.data.validation.TransactionValidator;
import cz.muni.fi.pv168.cashflowevidence.data.validation.Validator;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.RepeatingTransaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;

public abstract class CommonDependencyProvider implements DependencyProvider {

    private final DatabaseManager databaseManager;
    private final Validator<Category> categoryValidator;
    private final Validator<Transaction> transactionValidator;
    private final Validator<RepeatingTransaction> repeatingTransactionValidator;


    protected CommonDependencyProvider(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        this.categoryValidator = new CategoryValidator();
        this.transactionValidator = new TransactionValidator();
        this.repeatingTransactionValidator = new RepeatingTransactionValidator();
    }

    @Override
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
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
