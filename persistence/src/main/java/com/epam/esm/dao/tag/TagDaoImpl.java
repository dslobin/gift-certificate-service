package com.epam.esm.dao.tag;

import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TagDaoImpl implements TagDao {
    @Override
    public List<Tag> findAll() {
        return null;
    }

    @Override
    public Optional<Tag> findById(long tagId) {
        return Optional.empty();
    }

    @Override
    public void save(Tag tag) {

    }

    @Override
    public void deleteById(long tagId) {

    }
}
