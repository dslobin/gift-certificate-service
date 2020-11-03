package com.epam.esm.service;

import com.epam.esm.entity.Tag;

import java.util.Optional;
import java.util.Set;

public interface TagService {
    Set<Tag> findAll();

    Optional<Tag> findById(long id);

    Tag create(String tagName);

    void deleteById(long id);
}
