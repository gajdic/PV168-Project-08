package cz.muni.fi.pv168.cashflowevidence.wiring;

import cz.muni.fi.pv168.cashflowevidence.data.manipulation.Exporter;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.Importer;
import cz.muni.fi.pv168.cashflowevidence.data.storage.db.DatabaseManager;

import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.CategoryRepository;
import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.RepeatingTransactionRepository;
import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.Repository;
import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.TransactionRepository;
import cz.muni.fi.pv168.cashflowevidence.data.validation.Validator;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.RepeatingTransaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;

public interface DependencyProvider {

    DatabaseManager getDatabaseManager();
    Repository<Category> getCategoryRepository();
    Repository<Transaction> getTransactionRepository();
    Repository<RepeatingTransaction> getRepeatingTransactionRepository();
    Validator<Category> getCategoryValidator();
    Validator<Transaction> getTransactionValidator();
    Validator<RepeatingTransaction> getRepeatingTransactionValidator();
    Importer getImporter();
    Exporter getExporter();
}
