package com.epam.esm.service.impl;

import com.epam.esm.dao.tag.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {
    private TagDao tagDao;
    private TagService tagService;

    @BeforeEach
    void setUp() {
        tagDao = mock(TagDao.class);
        tagService = new TagServiceImpl(tagDao);
    }

    @Test
    void shouldSaveTagSuccessfully() {
        Tag tag = new Tag(1L, "Quests");

        given(tagDao.save(any(Tag.class))).willReturn(tag.getId());

        Tag savedTag = tagService.create(tag);

        assertThat(savedTag).isNotNull();
        int wantedNumberOfInvocations = 1;
        verify(tagDao, times(wantedNumberOfInvocations)).save(any(Tag.class));
    }

    @Test
    void shouldReturnAllTags() {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag(1L, "Fitness_and_sports"));
        tags.add(new Tag(2L, "Beauty_salon_service"));
        tags.add(new Tag(3L, "Travel"));

        given(tagDao.findAll()).willReturn(tags);

        Set<Tag> expectedTags = tagService.findAll();

        assertEquals(expectedTags, tags);
    }

    @Test
    void shouldReturnTagById() {
        long tagId = 1L;
        Tag tag = new Tag(1L, "Exclusive");

        given(tagDao.findById(tagId)).willReturn(Optional.of(tag));

        Optional<Tag> expectedTag = tagService.findById(tagId);

        assertThat(expectedTag).isNotNull();
    }

    @Test
    void shouldDeleteTag() {
        long tagId = 1L;

        tagService.deleteById(tagId);

        int wantedNumberOfInvocations = 1;
        verify(tagDao, times(wantedNumberOfInvocations)).deleteById(tagId);
    }
}
