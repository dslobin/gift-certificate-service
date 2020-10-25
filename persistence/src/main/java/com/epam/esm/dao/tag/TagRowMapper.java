package com.epam.esm.dao.tag;

import com.epam.esm.entity.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TagRowMapper implements RowMapper<Tag> {
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";

    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tag tag = new Tag();
        tag.setId(rs.getLong(COLUMN_ID));
        tag.setName(rs.getString(COLUMN_NAME));
        return tag;
    }
}
