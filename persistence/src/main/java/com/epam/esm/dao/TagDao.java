package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

import java.util.Optional;
import java.util.Set;

public interface TagDao {
    /**
     * Returns all tags.
     *
     * @return all tags
     */
    Set<Tag> findAll(int page, int size);

    /**
     * Retrieves a tag by its name.
     *
     * @param name unique tag identifier.
     * @return the tag with the given name or {@literal Optional#empty()} if none found.
     */
    Optional<Tag> findByName(String name);

    /**
     * Retrieves a tag by its id.
     *
     * @param id unique tag identifier.
     * @return the tag with the given id or {@literal Optional#empty()} if none found.
     */
    Optional<Tag> findById(long id);

    /**
     * Saves a given tag.
     *
     * @return the saved tag id.
     */
    long save(Tag tag);

    /**
     * Deletes the tag with the given id.
     *
     * @param id unique tag identifier.
     */
    void deleteById(long id);
}
