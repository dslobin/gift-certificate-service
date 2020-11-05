package com.epam.esm.dao.tag;

import com.epam.esm.dao.certificate.GiftCertificateDao;
import com.epam.esm.dao.config.JdbcContextConfiguration;
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
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JdbcContextConfiguration.class, loader = AnnotationConfigContextLoader.class)
@Sql(scripts = {"classpath:schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class JdbcDaoTest {
    @Autowired
    private TagDao tagDao;
    @Autowired
    private GiftCertificateDao certificateDao;

    @Test
    void givenTag_whenSave_thenGetOk() {
        long tagId = 1;
        String tagName = "Photo_session";
        Tag tag = new Tag(tagId, tagName);
        tagDao.save(tag.getName());

        Tag tagFromDb = tagDao.findById(tagId).get();
        assertEquals(tagName, tagFromDb.getName());
    }

    @Test
    void givenMultipleTags_whenSave_thenGetCorrectCount() {
        Set<Tag> tags = Stream.of(
                new Tag(1L, "Active_rest"),
                new Tag(2L, "Romantic"),
                new Tag(3L, "Motorists")
        ).collect(Collectors.toSet());

        tags.forEach(tag -> tagDao.save(tag.getName()));

        Set<Tag> tagsFromDb = tagDao.findAll();
        int expectedTagsCount = 3;
        assertEquals(expectedTagsCount, tagsFromDb.size());
    }

    @Test
    void givenCertificateWithTags_whenSave_thenGetCorrectTagCount() {
        Set<Tag> tags = Stream.of(
                new Tag(1L, "Exclusive"),
                new Tag(2L, "Business")
        ).collect(Collectors.toSet());
        GiftCertificate certificate = createCertificate(tags);
        certificateDao.save(certificate);

        tags.forEach(tag -> tagDao.save(tag.getName()));

        long certificateId = 1;
        tags.forEach(tag -> certificateDao.saveCertificateTag(certificateId, tag.getId()));

        GiftCertificate certificateFromDb = certificateDao.findById(certificateId).get();
        Set<Tag> tagsFromSavedCertificate = certificateFromDb.getTags();
        assertEquals(tags.size(), tagsFromSavedCertificate.size());
    }

    private GiftCertificate createCertificate(Set<Tag> certificateTags) {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("Gift Certificate for business");
        certificate.setDescription("If you’re in business, you know that gift certificate cards are a must.");
        certificate.setPrice(BigDecimal.valueOf(11.99));
        certificate.setCreateDate(ZonedDateTime.now());
        certificate.setDuration(Duration.ofDays(7));
        certificate.setTags(certificateTags);
        return certificate;
    }
}
