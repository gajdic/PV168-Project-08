package cz.muni.fi.pv168.cashflowevidence.data.storage.repository;

import java.util.List;
import java.util.Optional;

/**
 * Represent a repository for a model
 *
 * @param <M> type of the model
 */
public interface Repository<M> {

    int getSize();

    Optional<M> findByID(long id);

    Optional<M> findByIndex(int index);

    List<M> findAll();

    void create(M newEntity);

    void deleteByIndex(int index);

    void update(M entity);

    void refresh();
}
