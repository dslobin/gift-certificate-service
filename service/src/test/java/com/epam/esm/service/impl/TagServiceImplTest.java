package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.mapper.TagMapper;
import com.epam.esm.service.TagService;
import com.epam.esm.service.config.ServiceContextTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = ServiceContextTest.class)
class TagServiceImplTest {
    @Autowired
    private TagDao tagDao;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private TagService tagService;

    @Test
    void givenTagDto_whenSave_thenGetOk() {
        Tag tag = new Tag(1L, "quests", null);

        given(tagDao.save(any(Tag.class))).willReturn(tag);

        TagDto tagDto = new TagDto(1L, "quests");

        TagDto savedTag = tagService.create(tagDto);
        assertThat(savedTag).isNotNull();
        assertEquals(tag.getName(), savedTag.getName());
    }

    @Test
    void givenTags_whenFindAll_thenGetCorrectTagSize() {
        Set<Tag> tags = Stream.of(
                new Tag(1L, "fitness_and_sports", null),
                new Tag(2L, "beauty_salon_service", null),
                new Tag(3L, "travel", null)
        ).collect(Collectors.toSet());

        int page = 1;
        int size = 5;
        doReturn(tags).when(tagDao).findAll(page, size);

        Set<TagDto> actualTags = tagService.findAll(page, size);
        int expectedTagsSize = tags.size();
        int actualTagsSize = actualTags.size();
        assertEquals(expectedTagsSize, actualTagsSize);
    }

    @Test
    void givenTag_whenFindById_thenGetCorrectTag() {
        long tagId = 1L;
        Tag tag = new Tag(1L, "exclusive", null);

        given(tagDao.findById(tagId)).willReturn(Optional.of(tag));

        TagDto actualTag = tagService.findById(tagId);

        assertThat(actualTag).isNotNull();
        assertEquals(tag.getId(), actualTag.getId());
    }

    @Test
    void givenTag_whenFindByName_thenGetCorrectTag() {
        long tagId = 1L;
        String tagName = "exclusive";
        Tag tag = new Tag(tagId, tagName, null);

        given(tagDao.findByName(tagName)).willReturn(Optional.of(tag));

        TagDto actualTag = tagService.findByName(tagName);

        assertThat(actualTag).isNotNull();
        assertEquals(tag.getName(), actualTag.getName());
    }

    @Test
    void givenTagId_whenDeleteById_thenGetOk() {
        long tagId = 1;

        tagService.deleteById(tagId);

        int wantedNumberOfInvocations = 1;
        verify(tagDao, times(wantedNumberOfInvocations)).deleteById(tagId);
    }
}
