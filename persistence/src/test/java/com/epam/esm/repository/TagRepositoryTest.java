package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TagRepositoryTest {
    @Autowired
    private TagRepository tagRepository;

    @Test
    @Sql(scripts = {"/test-tags-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenMultipleTags_whenFindAll_thenGetCorrectCount() {
        Set<Tag> actualTags = new HashSet<>(tagRepository.findAll());
        int expectedTagsCount = 5;
        assertEquals(expectedTagsCount, actualTags.size());
    }

    @Test
    @Sql(scripts = {"/test-tags-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenMultipleTags_whenFindByName_thenGetCorrectTag() {
        String requiredTagName = "travel";
        Optional<Tag> actualTag = tagRepository.findByName(requiredTagName);

        assertTrue(actualTag.isPresent());

        String actualTagName = actualTag.get().getName();
        assertEquals(requiredTagName, actualTagName);
    }

    @Test
    @Sql(scripts = {"/test-tags-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenMultipleTags_whenFindById_thenGetCorrectTag() {
        long requiredTagId = 3;
        Optional<Tag> actualTag = tagRepository.findById(requiredTagId);

        assertTrue(actualTag.isPresent());

        long actualTagId = actualTag.get().getId();
        assertEquals(requiredTagId, actualTagId);
    }

    @Test
    void givenTag_whenSave_thenGetCorrectTagName() {
        String activeRest = "active_rest";
        Tag tag = new Tag();
        tag.setName(activeRest);

        Tag savedTag = tagRepository.save(tag);

        assertFalse(Objects.isNull(savedTag));
        assertEquals(activeRest, savedTag.getName());
    }

    @Test
    @Sql(scripts = {"/test-tags-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenTagId_whenDelete_thenGetOk() {
        long requiredTagId = 1;
        tagRepository.deleteById(requiredTagId);
    }
}
