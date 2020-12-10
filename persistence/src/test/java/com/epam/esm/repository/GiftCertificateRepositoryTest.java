package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class GiftCertificateRepositoryTest {
    @Autowired
    private GiftCertificateRepository certificateDao;

    @Test
    @Sql(scripts = {"/test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenMultipleCertificates_whenFindAll_thenGetCorrectCount() {
        List<GiftCertificate> actualCertificates = certificateDao.findAll();

        int expectedCertificatesCount = 5;
        assertEquals(expectedCertificatesCount, actualCertificates.size());
    }

    @Test
    @Sql(scripts = {"/test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenCertificateId_whenFindById_thenGetCorrectGiftCertificate() {
        long requiredCertificateId = 1;
        Optional<GiftCertificate> savedCertificate = certificateDao.findById(requiredCertificateId);

        assertTrue(savedCertificate.isPresent());

        long actualCertificateId = savedCertificate.get().getId();
        assertEquals(requiredCertificateId, actualCertificateId);
    }

    @Test
    void givenGiftCertificate_whenSave_thenGetCorrectGiftCertificateId() {
        String name = "Diving Lessons";
        String description = "Gift certificate entitles you to attend 3 diving lessons from a professional instructor";
        BigDecimal price = BigDecimal.valueOf(135.15);
        Duration duration = Duration.ofDays(31);
        Set<Tag> tags = createTags();
        GiftCertificate certificate = new GiftCertificate(0, name, description, price, null, null, duration, tags, true);
        Set<Tag> certificateTags = certificate.getTags();

        GiftCertificate savedCertificate = certificateDao.save(certificate);
        Set<Tag> savedCertificateTags = savedCertificate.getTags();

        assertEquals(certificateTags.size(), savedCertificateTags.size());
        long expectedTagId = 1;
        assertEquals(expectedTagId, savedCertificate.getId());
    }

    private Set<Tag> createTags() {
        Tag tagActiveRest = new Tag(0, "active_rest", null);
        Tag tagSport = new Tag(0, "sport", null);
        return Stream.of(tagActiveRest, tagSport)
                .collect(Collectors.toSet());
    }

    @Test
    @Sql(scripts = {"/test-one-certificate-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenGiftCertificateId_whenUpdateCertificate_thenGetCorrectGiftCertificate() {
        long certificateId = 1;
        Optional<GiftCertificate> certificateOptional = certificateDao.findById(certificateId);

        assertTrue(certificateOptional.isPresent());
        GiftCertificate certificate = certificateOptional.get();
        BigDecimal newPrice = BigDecimal.valueOf(100);
        certificate.setPrice(newPrice);

        GiftCertificate updatedCertificate = certificateDao.saveAndFlush(certificate);
        assertEquals(newPrice, updatedCertificate.getPrice());
        assertTrue(Objects.nonNull(updatedCertificate.getLastUpdateDate()));
    }

    @Test
    @Sql(scripts = {"/test-one-certificate-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenGiftCertificateId_whenDelete_thenGetOk() {
        long requiredCertificateId = 1;
        certificateDao.deleteById(requiredCertificateId);
    }
}
