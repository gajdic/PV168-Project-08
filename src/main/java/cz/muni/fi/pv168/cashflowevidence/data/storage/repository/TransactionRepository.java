package cz.muni.fi.pv168.cashflowevidence.data.storage.repository;

import cz.muni.fi.pv168.cashflowevidence.data.storage.dao.TransactionDao;
import cz.muni.fi.pv168.cashflowevidence.data.storage.entity.TransactionEntity;
import cz.muni.fi.pv168.cashflowevidence.data.storage.mapper.EntityMapper;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a repository for transactions
 */
public class TransactionRepository implements Repository<Transaction> {

    private List<Transaction> transactions = new ArrayList<>();

    private final TransactionDao dao;

    private final EntityMapper<TransactionEntity, Transaction> mapper;

    public TransactionRepository(TransactionDao dao,
                                 EntityMapper<TransactionEntity, Transaction> mapper) {
        this.dao = dao;
        this.mapper = mapper;
        this.refresh();
    }

    @Override
    public int getSize() {
        return transactions.size();
    }

    @Override
    public Optional<Transaction> findByID(long id) {
        return transactions.stream()
                .filter(t -> t.getId() == id)
                .findFirst();
    }

    @Override
    public Optional<Transaction> findByIndex(int index) {
        if (index >= 0 && index < getSize()) {
            return Optional.of(transactions.get(index));
        }
        return Optional.empty();
    }
    @Override
    public List<Transaction> findAll() {
        return Collections.unmodifiableList(transactions);
    }

    @Override
    public void create(Transaction newEntity) {
        Stream.of(newEntity)
                .map(mapper::mapToEntity)
                .map(dao::create)
                .map(mapper::mapToModel)
                .forEach(t -> transactions.add(t));
    }

    @Override
    public void deleteByIndex(int index) {
        dao.deleteById(transactions.get(index).getId());
        transactions.remove(index);
    }

    @Override
    public void refresh() {
        transactions = fetchAll();
    }

    private List<Transaction> fetchAll() {
        return dao.findAll().stream()
                .map(mapper::mapToModel)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
