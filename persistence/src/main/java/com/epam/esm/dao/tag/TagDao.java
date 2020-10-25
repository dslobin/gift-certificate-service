package com.epam.esm.dao.tag;

import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagDao {
    List<Tag> findAll();

    List<Tag> findAllByGiftCertificateId(long giftCertificateId);

    Optional<Tag> findByName(String name);

    Optional<Tag> findById(long id);

    void save(Tag tag);

    void deleteById(long id);
}
