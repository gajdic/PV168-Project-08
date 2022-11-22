package cz.muni.fi.pv168.cashflowevidence.data.storage.repository;

import cz.muni.fi.pv168.cashflowevidence.data.storage.dao.RepeatingTransactionDao;
import cz.muni.fi.pv168.cashflowevidence.data.storage.entity.RepeatingTransactionEntity;
import cz.muni.fi.pv168.cashflowevidence.data.storage.mapper.EntityMapper;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.RepeatingTransaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represent a repository for repeating transactions
 */
public class RepeatingTransactionRepository implements Repository<RepeatingTransaction>{

    private List<RepeatingTransaction> repeatingTransactions = new ArrayList<>();

    private final RepeatingTransactionDao dao;

    private final EntityMapper<RepeatingTransactionEntity, RepeatingTransaction> mapper;

    public RepeatingTransactionRepository(RepeatingTransactionDao dao,
                                          EntityMapper<RepeatingTransactionEntity, RepeatingTransaction> mapper) {
        this.dao = dao;
        this.mapper = mapper;
        this.refresh();
    }

    @Override
    public int getSize() {
        return repeatingTransactions.size();
    }

    @Override
    public Optional<RepeatingTransaction> findByID(long id) {
        return repeatingTransactions.stream()
                .filter(rt -> rt.getId() == id)
                .findFirst();
    }

    @Override
    public Optional<RepeatingTransaction> findByIndex(int index) {
        if (index >= 0 && index < getSize()) {
            return Optional.of(repeatingTransactions.get(index));
        }
        return Optional.empty();
    }

    @Override
    public List<RepeatingTransaction> findAll() {
        return Collections.unmodifiableList(repeatingTransactions);
    }

    @Override
    public void create(RepeatingTransaction newEntity) {
        Stream.of(newEntity)
                .map(mapper::mapToEntity)
                .map(dao::create)
                .map(mapper::mapToModel)
                .forEach(rt -> repeatingTransactions.add(rt));
    }

    @Override
    public void deleteByIndex(int index) {
        dao.deleteById(repeatingTransactions.get(index).getId());
        repeatingTransactions.remove(index);
    }

    @Override
    public void update(RepeatingTransaction entity) {
        RepeatingTransaction storedEntity = findByID(entity.getId()).orElseThrow();
        int index = repeatingTransactions.indexOf(storedEntity);

        Stream.of(entity)
                .map(mapper::mapToEntity)
                .map(dao::update)
                .map(mapper::mapToModel)
                .forEach(rt -> repeatingTransactions.set(index, rt));
    }

    @Override
    public void refresh() {
        repeatingTransactions = fetchAll();
    }

    private List<RepeatingTransaction> fetchAll() {
        return dao.findAll().stream()
                .map(mapper::mapToModel)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
