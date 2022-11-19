package cz.muni.fi.pv168.cashflowevidence.data.storage.repository;

import cz.muni.fi.pv168.cashflowevidence.model.transaction.RepeatingTransaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represent a repository for repeating transactions
 */
public class RepeatingTransactionRepository implements Repository<RepeatingTransaction>{

    private final List<RepeatingTransaction> repeatingTransactions = new ArrayList<>();

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public Optional<RepeatingTransaction> findByID(long id) {
        return Optional.empty();
    }

    @Override
    public Optional<RepeatingTransaction> findByIndex(int index) {
        return Optional.empty();
    }

    @Override
    public List<RepeatingTransaction> findAll() {
        return null;
    }

    @Override
    public void refresh() {

    }

    @Override
    public void create(RepeatingTransaction newEntity) {

    }

    @Override
    public void deleteByIndex(int index) {

    }
}
