package cz.muni.fi.pv168.cashflowevidence.wiring;

import cz.muni.fi.pv168.cashflowevidence.data.storage.db.DatabaseManager;
import cz.muni.fi.pv168.cashflowevidence.data.validation.Validator;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.RepeatingTransaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;

public interface DependencyProvider {

    DatabaseManager getDatabaseManager();

    Validator<Category> getCategoryValidator();
    Validator<Transaction> getTransactionValidator();
    Validator<RepeatingTransaction> getRepeatingTransactionValidator();

}
