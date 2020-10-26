package com.epam.esm.dao.certificate;

import com.epam.esm.dao.tag.TagDao;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcGiftCertificateDao implements GiftCertificateDao {
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    private TagDao tagDao;

    private static final String FIND_ALL =
            "SELECT gift_certificates.id, gift_certificates.name, gift_certificates.description," +
                    " gift_certificates.price, gift_certificates.create_date," +
                    " gift_certificates.last_update_date, gift_certificates.duration" +
                    " FROM gift_certificates";

    private static final String FIND_BY_ID =
            FIND_ALL + " WHERE id = ?";

    private static final String DELETE_BY_ID =
            "DELETE FROM gift_certificates WHERE id = ?";

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("gift_certificates")
                .usingGeneratedKeyColumns("id");
    }

    @Autowired
    public void setTagDao(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public List<GiftCertificate> findAll() {
        return jdbcTemplate.query(FIND_ALL, new GiftCertificateRowMapper(tagDao));
    }

    @Override
    public Optional<GiftCertificate> findById(long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID, new GiftCertificateRowMapper(tagDao), id));
    }

    @Override
    public void save(GiftCertificate giftCertificate) {
        Map<String, Object> certificateParams = getCertificateParameters(giftCertificate);
        this.jdbcInsert.execute(certificateParams);
    }

    private Map<String, Object> getCertificateParameters(GiftCertificate certificate) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", certificate.getId());
        parameters.put("name", certificate.getName());
        parameters.put("description", certificate.getDescription());
        parameters.put("price", certificate.getPrice());
        parameters.put("create_date", certificate.getCreateDate());
        parameters.put("last_update_date", certificate.getLastUpdateDate());
        parameters.put("duration", certificate.getDuration());
        return parameters;
    }

    @Override
    public void deleteById(long id) {
        jdbcTemplate.update(DELETE_BY_ID, id);
    }
}
