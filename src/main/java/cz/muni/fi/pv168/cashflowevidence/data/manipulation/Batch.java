package cz.muni.fi.pv168.cashflowevidence.data.manipulation;

import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;

import java.util.Collection;

public class Batch {
    private final Collection<Category> categories;
    private final Collection<Transaction> transactions;

    public Batch(Collection<Category> categories, Collection<Transaction> transactions) {
        this.categories = categories;
        this.transactions = transactions;
    }

    public Collection<Category> getCategories() {
        return categories;
    }

    public Collection<Transaction> getTransactions() {
        return transactions;
    }
}
