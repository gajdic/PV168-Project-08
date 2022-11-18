package cz.muni.fi.pv168.cashflowevidence.data.storage.dao;

import cz.muni.fi.pv168.cashflowevidence.model.transaction.RepeatingTransaction;

import java.util.Collection;

public class RepeatingTransactionDao implements DatabaseAccessObject<RepeatingTransaction> {
    @Override
    public RepeatingTransaction create(RepeatingTransaction entity) {
        return null;
    }

    @Override
    public void deleteById(long id) {}

    @Override
    public Collection<RepeatingTransaction> findAll() {
        return null;
    }

    @Override
    public RepeatingTransaction findById(long id) {
        return null;
    }
}
