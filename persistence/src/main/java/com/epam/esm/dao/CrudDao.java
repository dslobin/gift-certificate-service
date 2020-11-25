package com.epam.esm.dao;

import java.util.List;
import java.util.Optional;

public interface CrudDao<T, ID> {
    /**
     * Returns all instances of the type.
     *
     * @return all entities
     */
    List<T> findAll();

    /**
     * Retrieves an entity by its id.
     *
     * @param id unique entity identifier.
     * @return the entity with the given id or {@literal Optional#empty()} if none found.
     */
    Optional<T> findById(ID id);

    /**
     * Saves a given entity.
     *
     * @return the saved entity
     */
    T save(T entity);

    /**
     * Updates a given entity.
     *
     * @param entity must not be {@literal null}.
     */
    T update(T entity);

    /**
     * Deletes a given entity.
     *
     * @param entity must not be {@literal null}.
     */
    void delete(T entity);

    /**
     * Deletes the entity with the given id.
     *
     * @param id unique entity identifier.
     */
    void deleteById(ID id);
}
