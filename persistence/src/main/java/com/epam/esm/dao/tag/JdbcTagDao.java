package com.epam.esm.dao.tag;

import com.epam.esm.entity.Tag;
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
public class JdbcTagDao implements TagDao {
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    private static final String FIND_ALL =
            "SELECT id, name FROM tags";

    private static final String FIND_ALL_BY_GIFT_CERTIFICATE_ID =
            "SELECT tags.id, tags.name" +
                    " FROM gift_certificate_tag" +
                    " INNER JOIN tags" +
                    " ON gift_certificate_tag.tag_id = tags.id" +
                    " WHERE gift_certificate_tag.gift_certificate_id = ?";

    private static final String FIND_BY_ID =
            FIND_ALL + " WHERE id = ?";

    private static final String FIND_BY_NAME =
            FIND_ALL + " WHERE name = ?";

    private static final String DELETE_BY_ID =
            "DELETE FROM tags WHERE id = ?";

    @Autowired
    @Override
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("tags")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Tag> findAll() {
        return jdbcTemplate.query(FIND_ALL, new TagRowMapper());
    }

    @Override
    public List<Tag> findAllByGiftCertificateId(long giftCertificateId) {
        return jdbcTemplate.query(FIND_ALL_BY_GIFT_CERTIFICATE_ID, new Object[]{giftCertificateId}, new TagRowMapper());
    }

    @Override
    public Optional<Tag> findByName(String name) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_NAME, new TagRowMapper(), name));
    }

    @Override
    public Optional<Tag> findById(long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID, new TagRowMapper(), id));
    }

    @Override
    public long save(Tag tag) {
        Map<String, Object> tagParams = new HashMap<>();
        tagParams.put("name", tag.getName());
        Number tagId = this.jdbcInsert.executeAndReturnKey(tagParams);
        return tagId.longValue();
    }

    @Override
    public void deleteById(long id) {
        jdbcTemplate.update(DELETE_BY_ID, id);
    }
}
