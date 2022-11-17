package cz.muni.fi.pv168.cashflowevidence.data.storage.dao;

import java.util.Collection;

/**
 * Represents a generic interface used for CRUD operations.
 *
 * @param <T>
 */
public interface DatabaseAccessObject<T> {
    /**
     *
     * @param entity entity to be created
     * @return instance of an entity object
     */
    T create(T entity);

    /**
     * Removes entity by the provided entityId
     *
     * @param id id of the entity
     */
    void deleteById(long id);

    /**
     * Gets all entitiaes.
     *
     * @return Collection containing all entities
     */
    Collection<T> findAll();

    /**
     * Gets entity with the provided id.
     *
     * @param id of entity to select.
     * @return Entity instance with the provided id.
     */
    T findById(long id);
}
