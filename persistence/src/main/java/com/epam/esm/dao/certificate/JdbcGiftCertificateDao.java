package com.epam.esm.dao.certificate;

import com.epam.esm.dao.tag.TagDao;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class JdbcGiftCertificateDao implements GiftCertificateDao {
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    private NamedParameterJdbcTemplate namedJdbcTemplate;
    private TagDao tagDao;

    private static final String SELECT_CERTIFICATES_WITH_TAGS =
            "SELECT gift_certificates.id, gift_certificates.name, gift_certificates.description," +
                    " gift_certificates.price, gift_certificates.create_date, gift_certificates.last_update_date," +
                    " gift_certificates.duration, tags.id AS tag_id, tags.name AS tag_name" +
                    " FROM gift_certificates" +
                    " JOIN gift_certificate_tag" +
                    " ON gift_certificates.id = gift_certificate_tag.gift_certificate_id" +
                    " JOIN tags" +
                    " ON gift_certificate_tag.tag_id = tags.id";

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
    @Override
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("gift_certificates")
                .usingGeneratedKeyColumns("id");
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Autowired
    public void setTagDao(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public List<GiftCertificate> findAll() {
        return jdbcTemplate.query(
                FIND_ALL,
                new GiftCertificateRowMapper(tagDao)
        );
    }

    @Override
    public List<GiftCertificate> findAll(String tag, String name, String description, String sort) {
        StringBuilder sql = new StringBuilder(SELECT_CERTIFICATES_WITH_TAGS);
        String whereClause = prepareWhereClause(tag, name, description);
        sql.append(whereClause);
        String orderClause = prepareOrderClause(sort);
        sql.append(orderClause);
        Map<String, Object> params = new HashMap<>();
        params.put("tag", tag);
        params.put("name", name);
        params.put("description", description);
        return namedJdbcTemplate.query(
                sql.toString(),
                params,
                new GiftCertificateRowMapper(tagDao)
        );
    }

    private String prepareWhereClause(String tag, String name, String description) {
        StringJoiner whereClause = new StringJoiner(" AND ", " WHERE ", "").setEmptyValue("");
        if (tag != null) {
            String whereTagEquals = "tags.name = :tag";
            whereClause.add(whereTagEquals);
        }
        if (name != null) {
            String whereCertificateNameLike = "gift_certificates.name ILIKE '%' || :name || '%'";
            whereClause.add(whereCertificateNameLike);
        }
        if (description != null) {
            String whereCertificateDescriptionLike = "gift_certificates.description ILIKE '%' || :description  || '%'";
            whereClause.add(whereCertificateDescriptionLike);
        }
        return whereClause.toString();
    }

    private String prepareOrderClause(String sort) {
        String empty = " ";
        if (sort == null) {
            return empty;
        }
        String[] params = sort.split(",");
        return String.format(" ORDER BY %s %s", params[0], params[1]);
    }

    @Override
    public Optional<GiftCertificate> findById(long id) {
        GiftCertificate certificate = DataAccessUtils.singleResult(jdbcTemplate.query(
                FIND_BY_ID,
                new Object[]{id},
                new GiftCertificateRowMapper(tagDao)));
        return Optional.ofNullable(certificate);
    }

    @Override
    public void saveCertificateTag(long certificateId, long tagId) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("gift_certificate_tag");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("gift_certificate_id", certificateId);
        parameters.put("tag_id", tagId);
        simpleJdbcInsert.execute(parameters);
    }

    @Override
    public long save(GiftCertificate giftCertificate) {
        Map<String, Object> certificateParams = getCertificateParameters(giftCertificate);
        Number certificateId = this.jdbcInsert.executeAndReturnKey(certificateParams);
        return certificateId.longValue();
    }

    private Map<String, Object> getCertificateParameters(GiftCertificate certificate) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", certificate.getId());
        parameters.put("name", certificate.getName());
        parameters.put("description", certificate.getDescription());
        parameters.put("price", certificate.getPrice());
        parameters.put("create_date", certificate.getCreateDate());
        parameters.put("last_update_date", certificate.getLastUpdateDate());
        parameters.put("duration", certificate.getDuration().toDays());
        return parameters;
    }

    @Override
    public void deleteById(long id) {
        jdbcTemplate.update(DELETE_BY_ID, id);
    }
}
