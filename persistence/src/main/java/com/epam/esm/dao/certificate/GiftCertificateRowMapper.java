package com.epam.esm.dao.certificate;

import com.epam.esm.dao.tag.TagDao;
import com.epam.esm.entity.GiftCertificate;
import org.postgresql.util.PGInterval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;

public class GiftCertificateRowMapper implements RowMapper<GiftCertificate> {
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_CREATE_DATE = "create_date";
    private static final String COLUMN_LAST_UPDATE_DATE = "last_update_date";
    private static final String COLUMN_DURATION = "duration";

    private TagDao tagDao;

    @Autowired
    public GiftCertificateRowMapper(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        GiftCertificate certificate = new GiftCertificate();
        long certificateId = rs.getLong(COLUMN_ID);
        certificate.setId(certificateId);
        certificate.setName(rs.getString(COLUMN_NAME));
        certificate.setDescription(rs.getString(COLUMN_DESCRIPTION));
        certificate.setPrice(rs.getBigDecimal(COLUMN_PRICE));
        certificate.setCreateDate(rs.getDate(COLUMN_CREATE_DATE).toLocalDate());
        if (rs.getDate(COLUMN_LAST_UPDATE_DATE) != null) {
            certificate.setLastUpdateDate(rs.getDate(COLUMN_LAST_UPDATE_DATE).toLocalDate());
        }
        certificate.setDuration(intervalToDuration(rs.getString(COLUMN_DURATION)));
        certificate.setTags(tagDao.findAllByGiftCertificateId(certificateId));
        return certificate;
    }

    private Duration intervalToDuration(String daysAmount) throws SQLException {
        PGInterval interval = new PGInterval(daysAmount);
        return Duration.ofDays(interval.getDays());
    }
}
