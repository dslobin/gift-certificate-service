package com.epam.esm.service;

import com.epam.esm.entity.Tag;

import java.util.Optional;
import java.util.Set;

public interface TagService {
    Set<Tag> findAll(int page, int size);

    Optional<Tag> findById(long id);

    Optional<Tag> findByName(String name);

    Tag create(Tag tag);

    void deleteById(long id);
}
