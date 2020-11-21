package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
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
    public Optional<TagDto> findById(long id) {
        Tag tag = tagDao.findById(id).orElse(null);
        return Optional.ofNullable(tagMapper.toDto(tag));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TagDto> findByName(String name) {
        Tag tag = tagDao.findByName(name).orElse(null);
        return Optional.ofNullable(tagMapper.toDto(tag));
    }

    @Override
    @Transactional
    public TagDto create(TagDto tagDto) {
        Tag tag = tagMapper.toModel(tagDto);
        long tagId = tagDao.save(tag);
        tag.setId(tagId);
        return tagMapper.toDto(tag);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        tagDao.deleteById(id);
    }
}
