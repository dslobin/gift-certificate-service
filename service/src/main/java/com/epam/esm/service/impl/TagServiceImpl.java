package com.epam.esm.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.NameAlreadyExistException;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagServiceImpl implements TagService {
    private final TagRepository tagDao;

    private static final String TAG_ID_NOT_FOUND = "error.notFound.tagId";
    private static final String TAG_NAME_EXIST = "error.badRequest.tagNameExist";

    @Override
    @Transactional(readOnly = true)
    public Set<Tag> findAll(Pageable pageable) {
        return tagDao.findAll(pageable).get()
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public Tag findById(long id)
            throws TagNotFoundException {
        return tagDao.findById(id)
                .orElseThrow(() -> new TagNotFoundException(TAG_ID_NOT_FOUND, id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tag> findByName(String name) {
        return tagDao.findByName(name);
    }

    @Override
    @Transactional
    public Tag create(Tag tag)
            throws NameAlreadyExistException {
        Optional<Tag> existedTag = tagDao.findByName(tag.getName());
        if (existedTag.isPresent()) {
            String existedName = existedTag.get().getName();
            log.error("A tag named '{}' already exists", existedName);
            throw new NameAlreadyExistException(TAG_NAME_EXIST, existedName);
        }
        return tagDao.save(tag);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        tagDao.deleteById(id);
    }
}
