package cz.muni.fi.pv168.cashflowevidence.data.storage.dao;

import cz.muni.fi.pv168.cashflowevidence.exceptions.DataStorageException;
import java.util.Collection;

/**
 * Represents a generic interface used for CRUD operations.
 *
 * @param <T>
 */
public interface DatabaseAccessObject<T> {
    /**
     * Saves provided entity into the database.
     *
     * @param entity entity to be created
     * @throws DataStorageException when something goes wrong with the datasource
     * @return instance of an entity object
     */
    T create(T entity);

    /**
     * Removes entity by the provided entityId
     *
     * @throws DataStorageException when something goes wrong with the datasource
     * @param id id of the entity
     */
    void deleteById(long id);

    /**
     * Gets all entities.
     *
     * @throws DataStorageException when something goes wrong with the datasource
     * @return Collection containing all entities
     */
    Collection<T> findAll();

    /**
     * Gets entity with the provided id.
     *
     * @param id of entity to select.
     * @throws DataStorageException when something goes wrong with the datasource
     * @return Entity instance with the provided id.
     */
    T findById(long id);

    /**
     * Updates entity.
     *
     * @param entity Entity with new values.
     * @throws DataStorageException when something goes wrong with the datasource
     */
    T update(T entity);
}
