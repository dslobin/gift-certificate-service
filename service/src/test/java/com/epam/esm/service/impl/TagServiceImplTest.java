package com.epam.esm.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.NameAlreadyExistException;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.service.config.ServiceContextTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = ServiceContextTest.class)
class TagServiceImplTest {
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TagService tagService;

    @Test
    void givenTag_whenSave_thenGetOk() {
        Tag createdTag = new Tag(1L, "quests", null);

        given(tagRepository.save(any(Tag.class))).willReturn(createdTag);

        Tag tag = new Tag(0L, "quests", null);

        Tag savedTag = tagService.create(tag);
        assertThat(savedTag).isNotNull();
        assertEquals(createdTag.getName(), savedTag.getName());
    }

    @Test
    void givenTags_whenFindAll_thenGetCorrectTagSize() {
        List<Tag> tags = Stream.of(
                new Tag(1L, "fitness_and_sports", null),
                new Tag(2L, "beauty_salon_service", null),
                new Tag(3L, "travel", null)
        ).collect(Collectors.toList());

        int page = 0;
        int size = Integer.MAX_VALUE;
        PageRequest pageable = PageRequest.of(page, size);
        Page<Tag> tagPage = new PageImpl<>(tags, pageable, tags.size());
        doReturn(tagPage).when(tagRepository).findAll(pageable);

        Set<Tag> actualTags = tagService.findAll(pageable);
        int expectedTagsSize = tags.size();
        int actualTagsSize = actualTags.size();
        assertEquals(expectedTagsSize, actualTagsSize);
    }

    @Test
    void givenTag_whenFindById_thenGetCorrectTag() {
        long tagId = 1L;
        Tag tag = new Tag(1L, "exclusive", null);

        given(tagRepository.findById(tagId)).willReturn(Optional.of(tag));

        Tag actualTag = tagService.findById(tagId);

        assertThat(actualTag).isNotNull();
        assertEquals(tag.getId(), actualTag.getId());
    }

    @Test
    void givenTag_whenFindByName_thenGetCorrectTag() {
        long tagId = 1L;
        String tagName = "exclusive";
        Tag createdTag = new Tag(tagId, tagName, null);

        given(tagRepository.findByName(tagName)).willReturn(Optional.of(createdTag));

        Optional<Tag> tagOptional = tagService.findByName(tagName);
        assertTrue(tagOptional.isPresent());

        Tag actualTag = tagOptional.get();
        assertEquals(createdTag.getName(), actualTag.getName());
    }

    @Test
    void givenTag_whenCreate_thenGetNameAlreadyExistException() {
        long tagId = 1L;
        String tagName = "exclusive";
        Tag createdTag = new Tag(tagId, tagName, null);

        given(tagRepository.findByName(tagName)).willReturn(Optional.of(createdTag));

        assertThrows(NameAlreadyExistException.class, () -> {
            tagService.create(createdTag);
        });
    }

    @Test
    void givenTagId_whenFindById_thenThrowTagNotFoundException() {
        long tagId = 1L;
        given(tagRepository.findById(tagId)).willReturn(Optional.empty());

        assertThrows(TagNotFoundException.class, () -> {
            tagService.findById(tagId);
        });
    }

    @Test
    void givenTagId_whenDeleteById_thenGetOk() {
        long tagId = 1;

        tagService.deleteById(tagId);

        int wantedNumberOfInvocations = 1;
        verify(tagRepository, times(wantedNumberOfInvocations)).deleteById(tagId);
    }
}
