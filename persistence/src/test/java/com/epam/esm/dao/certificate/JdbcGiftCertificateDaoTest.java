package com.epam.esm.dao.certificate;

import com.epam.esm.dao.config.JdbcContextTest;
import com.epam.esm.dao.tag.TagDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JdbcContextTest.class, loader = AnnotationConfigContextLoader.class)
class JdbcGiftCertificateDaoTest {
    @Autowired
    private TagDao tagDao;

    @Autowired
    private GiftCertificateDao certificateDao;

    @Test
    @Sql(scripts = {"classpath:test-schema.sql", "/test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenMultipleCertificates_whenFindAll_thenGetCorrectCount() {
        List<GiftCertificate> actualCertificates = certificateDao.findAll();

        int expectedCertificatesCount = 5;
        assertEquals(expectedCertificatesCount, actualCertificates.size());
    }

    @Test
    @Sql(scripts = {"classpath:test-schema.sql", "/test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenMultipleCertificates_whenFindAllWithParameters_thenGetCorrectCount() {
        String paramName = "курс";
        List<GiftCertificate> actualCertificates = certificateDao.findAll(null, paramName, null, null, null);

        int expectedCertificatesCount = 2;
        assertEquals(expectedCertificatesCount, actualCertificates.size());
    }

    @Test
    @Sql(scripts = {"classpath:test-schema.sql", "/test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenCertificateId_whenFindById_thenGetCorrectGiftCertificate() {
        long requiredCertificateId = 1;
        Optional<GiftCertificate> actualCertificate = certificateDao.findById(requiredCertificateId);

        assertTrue(actualCertificate.isPresent());
        assertEquals(requiredCertificateId, actualCertificate.get().getId());
    }

    @Test
    @Sql(scripts = {"classpath:test-schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenGiftCertificate_whenSave_thenGetCorrectGiftCertificateId() {
        GiftCertificate certificate = buildCertificate();

        long insertedCertificateId = certificateDao.save(certificate);

        Set<Tag> certificateTags = certificate.getTags();

        certificateTags.forEach(tag -> {
            tagDao.save(tag.getName());
            certificateDao.saveCertificateTag(insertedCertificateId, tag.getId());
        });

        Set<Tag> savedTags = tagDao.findAllByGiftCertificateId(insertedCertificateId);
        boolean isCertificateIdNotZero = insertedCertificateId != 0;
        assertEquals(certificateTags.size(), savedTags.size());
        assertTrue(isCertificateIdNotZero);
    }

    @Test
    @Sql(scripts = {"classpath:test-schema.sql", "/test-certificates-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenNewGiftCertificate_whenUpdateCertificate_thenGetCorrectGiftCertificate() {
        GiftCertificate certificate = buildCertificate();

        long certificateId = certificate.getId();

        certificateDao.update(certificate);

        GiftCertificate updatedCertificate = certificateDao.findById(certificateId)
                .orElse(null);

        assertFalse(Objects.isNull(updatedCertificate));
        assertEquals(certificate.getName(), updatedCertificate.getName());
        assertEquals(certificate.getPrice(), updatedCertificate.getPrice());
    }

    private GiftCertificate buildCertificate() {
        long id = 1L;
        String name = "DIVING LESSONS";
        String description = "Gift certificate entitles you to attend 3 diving lessons from a professional instructor";
        BigDecimal price = BigDecimal.valueOf(135.15);
        ZonedDateTime createDate = ZonedDateTime.now();
        Duration duration = Duration.ofDays(31);
        Tag tagActiveRest = new Tag(1L, "active_rest");
        Tag tagSport = new Tag(2L, "sport");
        Set<Tag> tags = Stream.of(tagActiveRest, tagSport).collect(Collectors.toSet());
        return new GiftCertificate(id, name, description, price, createDate, null, duration, tags);
    }

    @Test
    @Sql(scripts = {"classpath:test-schema.sql", "/test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenGiftCertificateId_whenDelete_thenGetOk() {
        long requiredCertificateId = 1;
        certificateDao.deleteById(requiredCertificateId);
    }
}
