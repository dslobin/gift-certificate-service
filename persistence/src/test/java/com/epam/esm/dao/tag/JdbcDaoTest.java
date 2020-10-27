package com.epam.esm.dao.tag;

import com.epam.esm.dao.certificate.GiftCertificateDao;
import com.epam.esm.dao.certificate.JdbcGiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.sql.DataSource;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@Sql(scripts = {"classpath:schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class JdbcDaoTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        public GiftCertificateDao giftCertificateDao() {
            GiftCertificateDao jdbcCertificateDao = new JdbcGiftCertificateDao();
            jdbcCertificateDao.setDataSource(hsqlDataSource());
            return jdbcCertificateDao;
        }

        @Bean
        public TagDao tagDao() {
            TagDao jdbcTagDao = new JdbcTagDao();
            jdbcTagDao.setDataSource(hsqlDataSource());
            return jdbcTagDao;
        }

        @Bean
        public DataSource hsqlDataSource() {
            return new EmbeddedDatabaseBuilder()
                    .setType(EmbeddedDatabaseType.H2)
                    .setScriptEncoding("UTF-8")
                    .setName("gift-certificate-service-test")
                    .addScript("schema.sql")
                    .build();
        }
    }

    @Autowired
    private TagDao tagDao;

    @Autowired
    private GiftCertificateDao certificateDao;

    @Test
    void givenTag_whenSave_thenGetOk() {
        long tagId = 1;
        String tagName = "Photo_session";
        Tag tag = new Tag(tagId, tagName);
        tagDao.save(tag);

        Tag tagFromDb = tagDao.findById(tagId).get();
        assertEquals(tagName, tagFromDb.getName());
    }

    @Test
    void givenMultipleTags_whenSave_thenGetCorrectCount() {
        Tag tagActiveRest = new Tag(1L, "Active_rest");
        tagDao.save(tagActiveRest);

        Tag tagRomantic = new Tag(2L, "Romantic");
        tagDao.save(tagRomantic);

        Tag tagMotorists = new Tag(3L, "Motorists");
        tagDao.save(tagMotorists);

        List<Tag> tags = tagDao.findAll();
        int expectedTagsCount = 3;
        assertEquals(expectedTagsCount, tags.size());
    }

    @Test
    public void givenCertificateWithTags_whenSave_thenGetCorrectTagCount() {
        GiftCertificate certificate = new GiftCertificate();
        long certificateId = 1;
        certificate.setId(certificateId);
        certificate.setName("Gift Certificate for business");
        certificate.setDescription("If you’re in business, you know that gift certificate cards are a must.");
        certificate.setPrice(BigDecimal.valueOf(11.99));
        certificate.setCreateDate(LocalDate.now());
        certificate.setDuration(Duration.ofDays(7));
        List<Tag> tags = Arrays.asList(new Tag(1L, "Exclusive"), new Tag(2L, "Business"));
        certificate.setTags(tags);
        certificateDao.save(certificate);

        for (Tag tag : tags) {
            tagDao.save(tag);
        }

        for (Tag tag : tags) {
            certificateDao.saveCertificateTag(certificateId, tag.getId());
        }

        GiftCertificate certificateFromDb = certificateDao.findById(certificateId).get();
        List<Tag> tagsFromSavedCertificate = certificateFromDb.getTags();
        assertEquals(tags.size(), tagsFromSavedCertificate.size());
    }
}
