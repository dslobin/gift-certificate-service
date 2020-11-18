package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

import java.util.Optional;
import java.util.Set;

public interface TagDao {
    Set<Tag> findAll();

    Optional<Tag> findByName(String name);

    Optional<Tag> findById(long id);

    long save(Tag tag);

    void deleteById(long id);
}
