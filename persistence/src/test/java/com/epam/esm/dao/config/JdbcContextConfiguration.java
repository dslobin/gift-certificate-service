package com.epam.esm.dao.config;

import com.epam.esm.dao.certificate.GiftCertificateDao;
import com.epam.esm.dao.certificate.JdbcGiftCertificateDao;
import com.epam.esm.dao.tag.JdbcTagDao;
import com.epam.esm.dao.tag.TagDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:database-test.properties")
public class JdbcContextConfiguration {
    @Value("${datasource.scriptEncoding}")
    private String scriptEncoding;
    @Value("${datasource.name}")
    private String dbName;
    @Value("${datasource.schema}")
    private String schema;

    @Bean
    public GiftCertificateDao giftCertificateDao() {
        GiftCertificateDao jdbcCertificateDao = new JdbcGiftCertificateDao();
        jdbcCertificateDao.setDataSource(h2DataSource());
        return jdbcCertificateDao;
    }

    @Bean
    public TagDao tagDao() {
        TagDao jdbcTagDao = new JdbcTagDao();
        jdbcTagDao.setDataSource(h2DataSource());
        return jdbcTagDao;
    }

    @Bean
    public DataSource h2DataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding(scriptEncoding)
                .setName(dbName)
                .addScript(schema)
                .build();
    }
}
