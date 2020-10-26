package com.epam.esm.service;

import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService {
    List<Tag> findAll();

    Optional<Tag> findById(long id);

    void create(Tag tag);

    void deleteById(long id);
}
