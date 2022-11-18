package cz.muni.fi.pv168.cashflowevidence.data.storage.dao;

import cz.muni.fi.pv168.cashflowevidence.data.storage.entity.TransactionEntity;

import java.util.Collection;

public class TransactionDao implements DatabaseAccessObject<TransactionEntity> {
    @Override
    public TransactionEntity create(TransactionEntity entity) {
        return null;
    }

    @Override
    public void deleteById(long id) {}

    @Override
    public Collection<TransactionEntity> findAll() {
        return null;
    }

    @Override
    public TransactionEntity findById(long id) {
        return null;
    }
}
