package com.epam.esm.service.impl;

import com.epam.esm.dao.tag.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagDao tagDao;

    @Override
    @Transactional(readOnly = true)
    public Set<Tag> findAll() {
        return tagDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tag> findById(long id) {
        return tagDao.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tag> findByName(String name) {
        return tagDao.findByName(name);
    }

    @Override
    @Transactional
    public Tag create(String tagName) {
        long tagId = tagDao.save(tagName);
        return new Tag(tagId, tagName);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        tagDao.deleteById(id);
    }
}
