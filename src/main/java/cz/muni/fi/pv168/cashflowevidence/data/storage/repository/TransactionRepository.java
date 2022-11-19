package cz.muni.fi.pv168.cashflowevidence.data.storage.repository;

import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a repository for transactions
 */
public class TransactionRepository implements Repository<Transaction> {

    private List<Transaction> transactions = new ArrayList<>();

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public Optional<Transaction> findByID(long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Transaction> findByIndex(int index) {
        return Optional.empty();
    }

    @Override
    public List<Transaction> findAll() {
        return null;
    }

    @Override
    public void refresh() {
    }

    @Override
    public void create(Transaction newEntity) {
    }

    @Override
    public void deleteByIndex(int index) {
    }
}
