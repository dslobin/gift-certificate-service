package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.NameAlreadyExistException;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.mapper.TagMapper;
import com.epam.esm.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagDao tagDao;
    private final TagMapper tagMapper;

    @Override
    @Transactional(readOnly = true)
    public Set<TagDto> findAll(int page, int size) {
        return tagDao.findAll(page, size).stream()
                .map(tagMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public TagDto findById(long id) {
        Tag tag = tagDao.findById(id)
                .orElseThrow(() -> new TagNotFoundException(id));
        return tagMapper.toDto(tag);
    }

    @Override
    @Transactional(readOnly = true)
    public TagDto findByName(String name) {
        Tag tag = tagDao.findByName(name)
                .orElseThrow(() -> new TagNotFoundException(name));
        return tagMapper.toDto(tag);
    }

    @Override
    @Transactional
    public TagDto create(TagDto tagDto) {
        Optional<Tag> existedTag = tagDao.findByName(tagDto.getName());
        if (existedTag.isPresent()) {
            String existedName = existedTag.get().getName();
            throw new NameAlreadyExistException(existedName);
        }
        Tag tag = tagMapper.toModel(tagDto);
        Tag createdTag = tagDao.save(tag);
        return tagMapper.toDto(createdTag);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        tagDao.deleteById(id);
    }
}
