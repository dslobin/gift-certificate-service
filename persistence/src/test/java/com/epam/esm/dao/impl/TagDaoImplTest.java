package com.epam.esm.dao.tag;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.config.JpaContextTest;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JpaContextTest.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class JdbcTagDaoTest {
    @Autowired
    private TagDao tagDao;

    @Test
    @Sql(scripts = {"/test-tags-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenMultipleTags_whenFindAll_thenGetCorrectCount() {
        int page = 1;
        int size = 5;
        Set<Tag> actualTags = tagDao.findAll(page, size);

        int expectedTagsCount = 5;
        assertEquals(expectedTagsCount, actualTags.size());
    }

    @Test
    @Sql(scripts = {"/test-tags-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenMultipleTags_whenFindByName_thenGetCorrectTag() {
        String requiredTagName = "travel";
        Optional<Tag> actualTag = tagDao.findByName(requiredTagName);

        assertTrue(actualTag.isPresent());

        String actualTagName = actualTag.get().getName();
        assertEquals(requiredTagName, actualTagName);
    }

    @Test
    @Sql(scripts = {"/test-tags-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenMultipleTags_whenFindById_thenGetCorrectTag() {
        long requiredTagId = 3;
        Optional<Tag> actualTag = tagDao.findById(requiredTagId);

        assertTrue(actualTag.isPresent());

        long actualTagId = actualTag.get().getId();
        assertEquals(requiredTagId, actualTagId);
    }

    @Test
    void givenTag_whenSave_thenGetCorrectTagName() {
        String activeRest = "active_rest";
        Tag tag = new Tag();
        tag.setName(activeRest);

        Tag savedTag = tagDao.save(tag);

        assertFalse(Objects.isNull(savedTag));
        assertEquals(activeRest, savedTag.getName());
    }

    @Test
    @Sql(scripts = {"/test-tags-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenTagId_whenDelete_thenGetOk() {
        long requiredTagId = 1;
        tagDao.deleteById(requiredTagId);
    }
}
