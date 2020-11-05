package com.epam.esm.service.impl;

import com.epam.esm.dao.tag.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.NameAlreadyExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagDao tagDao;

    @Override
    public Set<Tag> findAll() {
        return tagDao.findAll();
    }

    @Override
    public Optional<Tag> findById(long id) {
        return tagDao.findById(id);
    }

    @Override
    public Tag create(String tagName) {
        Optional<Tag> tagFromDb = tagDao.findByName(tagName);
        if (tagFromDb.isPresent()) {
            throw new NameAlreadyExistException(String.format("Tag with name %s already exists", tagName));
        }
        long tagId = tagDao.save(tagName);
        return new Tag(tagId, tagName);
    }

    @Override
    public void deleteById(long id) {
        tagDao.deleteById(id);
    }
}
