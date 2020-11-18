package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
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
    public Set<Tag> findAll(int page, int size) {
        return tagDao.findAll(page, size);
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
    public Tag create(Tag tag) {
        long tagId = tagDao.save(tag);
        tag.setId(tagId);
        return tag;
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        tagDao.deleteById(id);
    }
}
