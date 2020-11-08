package com.epam.esm.dao.certificate;

import com.epam.esm.dao.OrderDirection;
import com.epam.esm.dao.tag.TagDao;
import com.epam.esm.entity.GiftCertificate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Repository
@Slf4j
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

    private static final String DELETE_CERTIFICATE_TAG =
            "DELETE FROM gift_certificate_tag WHERE tag_id = ? AND gift_certificate_id = ?";

    private static final String UPDATE =
            "UPDATE gift_certificates" +
                    " SET name = ?, description = ?, price = ?, last_update_date = ?, duration = ?" +
                    " WHERE id = ?";

    private static final String WHERE_TAG_EQUALS =
            "tags.name = :tag";

    private static final String WHERE_CERTIFICATE_NAME_LIKE =
            "gift_certificates.name ILIKE '%' || :name || '%'";

    private static final String WHERE_CERTIFICATE_DESCRIPTION_LIKE =
            "gift_certificates.description ILIKE '%' || :description  || '%'";

    private static final String ORDER_BY_CREATE_DATE_ASC =
            "ORDER BY create_date ASC";

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
    public List<GiftCertificate> findAll(
            String tag,
            String name,
            String description,
            String sortByName,
            String sortByCreateDate
    ) {
        String whereClause = prepareWhereClause(tag, name, description);
        String orderClause = prepareOrderClause(sortByName, sortByCreateDate);
        String sql = collectFinalStatement(orderClause, whereClause);
        log.debug("Sql: {}", sql);
        Map<String, Object> params = new HashMap<>();
        params.put("tag", tag);
        params.put("name", name);
        params.put("description", description);
        return namedJdbcTemplate.query(
                sql,
                params,
                new GiftCertificateRowMapper(tagDao)
        );
    }

    private String collectFinalStatement(String orderBy, String where) {
        StringBuilder findAllSqlStatement = new StringBuilder();
        if (StringUtils.isEmpty(where)) {
            findAllSqlStatement.append(FIND_ALL);
        } else {
            findAllSqlStatement.append(SELECT_CERTIFICATES_WITH_TAGS);
            findAllSqlStatement.append(where);
        }
        findAllSqlStatement.append(orderBy);
        return findAllSqlStatement.toString();
    }

    private String prepareWhereClause(String tag, String name, String description) {
        StringJoiner whereClause = new StringJoiner(" AND ", " WHERE ", "").setEmptyValue("");
        if (!StringUtils.isEmpty(tag)) {
            whereClause.add(WHERE_TAG_EQUALS);
        }
        if (!StringUtils.isEmpty(name)) {
            whereClause.add(WHERE_CERTIFICATE_NAME_LIKE);
        }
        if (!StringUtils.isEmpty(description)) {
            whereClause.add(WHERE_CERTIFICATE_DESCRIPTION_LIKE);
        }
        return whereClause.toString();
    }

    private String prepareOrderClause(String sortByName, String sortByCreateDate) {
        StringJoiner orderClause = new StringJoiner(", ", " ORDER BY ", "").setEmptyValue("");
        if (!StringUtils.isEmpty(sortByName)) {
            String orderByName = createSortingCondition(sortByName);
            orderClause.add(orderByName);
        }
        if (!StringUtils.isEmpty(sortByCreateDate)) {
            String orderByCreateDate = createSortingCondition(sortByCreateDate);
            orderClause.add(orderByCreateDate);
        }
        return orderClause.toString();
    }

    private String createSortingCondition(String sort) {
        String[] params = sort.split(",");
        log.debug("Sort parameters: " + Arrays.toString(params));
        if (params.length == 1) {
            return GiftCertificateOrderField.getOrderField(params[0]);
        }
        String field = GiftCertificateOrderField.getOrderField(params[0]);
        String direction = OrderDirection.getOrderDirection(params[1]);
        return String.format("%s %s", field, direction);
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
    public void deleteCertificateTag(long certificateId, long tagId) {
        jdbcTemplate.update(DELETE_CERTIFICATE_TAG, tagId, certificateId);
    }

    @Override
    public long save(GiftCertificate giftCertificate) {
        Map<String, Object> certificateParams = getCertificateParameters(giftCertificate);
        Number certificateId = this.jdbcInsert.executeAndReturnKey(certificateParams);
        return certificateId.longValue();
    }

    @Override
    public void update(GiftCertificate giftCertificate) {
        ZonedDateTime lastUpdateDate = giftCertificate.getLastUpdateDate() == null
                ? ZonedDateTime.now()
                : giftCertificate.getLastUpdateDate();
        jdbcTemplate.update(UPDATE,
                giftCertificate.getName(),
                giftCertificate.getDescription(),
                giftCertificate.getPrice(),
                convertToLocalDateTime(lastUpdateDate),
                giftCertificate.getDuration().toDays(),
                giftCertificate.getId());
    }

    private Map<String, Object> getCertificateParameters(GiftCertificate certificate) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", certificate.getId());
        parameters.put("name", certificate.getName());
        parameters.put("description", certificate.getDescription());
        parameters.put("price", certificate.getPrice());
        parameters.put("create_date", convertToLocalDateTime(certificate.getCreateDate()));
        parameters.put("last_update_date", null);
        parameters.put("duration", certificate.getDuration().toDays());
        return parameters;
    }

    private LocalDateTime convertToLocalDateTime(ZonedDateTime zonedDateTime) {
        if (zonedDateTime == null) {
            return null;
        }
        OffsetDateTime offsetDateTime = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault()).toOffsetDateTime();
        return offsetDateTime.toLocalDateTime();
    }

    @Override
    public void deleteById(long id) {
        jdbcTemplate.update(DELETE_BY_ID, id);
    }
}
