package com.epam.esm.dao.tag;

import com.epam.esm.dao.config.JdbcContextTest;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JdbcContextTest.class, loader = AnnotationConfigContextLoader.class)
class JdbcTagDaoTest {
    @Autowired
    private TagDao tagDao;

    @Test
    @Sql(scripts = {"classpath:test-schema.sql", "/test-tags-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenMultipleTags_whenFindAll_thenGetCorrectCount() {
        Set<Tag> actualTags = tagDao.findAll();

        int expectedTagsCount = 5;
        assertEquals(expectedTagsCount, actualTags.size());
    }

    @Test
    @Sql(scripts = {"classpath:test-schema.sql", "/test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenCertificateId_whenFindAllCertificateTags_thenGetCorrectTagsCount() {
        long certificateId = 1;
        Set<Tag> certificateTags = tagDao.findAllByGiftCertificateId(certificateId);

        int expectedTagsCount = 2;
        assertEquals(expectedTagsCount, certificateTags.size());
    }

    @Test
    @Sql(scripts = {"classpath:test-schema.sql", "/test-tags-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenMultipleTags_whenFindByName_thenGetCorrectTag() {
        String requiredTagName = "Travel";
        Optional<Tag> actualTag = tagDao.findByName(requiredTagName);

        assertTrue(actualTag.isPresent());
        assertEquals(requiredTagName, actualTag.get().getName());
    }

    @Test
    @Sql(scripts = {"classpath:test-schema.sql", "/test-tags-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenMultipleTags_whenFindById_thenGetCorrectTag() {
        long requiredTagId = 3;
        Optional<Tag> actualTag = tagDao.findById(requiredTagId);

        assertTrue(actualTag.isPresent());
        assertEquals(requiredTagId, actualTag.get().getId());
    }

    @Test
    @Sql(scripts = {"classpath:test-schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenTagName_whenSave_thenGetCorrectTagId() {
        String tagActiveRest = "ActiveRest";
        long insertedTagId = tagDao.save(tagActiveRest);

        boolean isTagIdNotZero = insertedTagId != 0;
        assertTrue(isTagIdNotZero);
    }

    @Test
    @Sql(scripts = {"classpath:test-schema.sql", "/test-tags-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenTagId_whenDelete_thenGetOk() {
        long requiredTagId = 1;
        tagDao.deleteById(requiredTagId);
    }
}
