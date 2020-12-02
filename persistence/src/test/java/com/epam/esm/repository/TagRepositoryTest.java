package com.epam.esm.repository;

import com.epam.esm.config.JpaContextTest;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = {JpaContextTest.class})
@EnableJpaRepositories(basePackages = {"com.epam.esm.repository"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class TagDaoImplTest {
    @Autowired
    private TagRepository tagRepository;

    private static int PAGE = 1;
    private static int SIZE = 30;

    @Test
    @Sql(scripts = {"/test-tags-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenMultipleTags_whenFindAll_thenGetCorrectCount() {
        Pageable pageable = PageRequest.of(PAGE, SIZE);
        Set<Tag> actualTags = tagRepository.findAll(pageable).toSet();

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
