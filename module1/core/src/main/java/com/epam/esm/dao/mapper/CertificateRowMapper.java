package com.epam.esm.dao.mapper;

import com.epam.esm.domain.Certificate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Component
public class CertificateRowMapper implements RowMapper<Certificate> {
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String CREATION_DATE = "creation_date";
    private static final String UPDATE_DATE = "update_date";
    private static final String STATE = "state";
    private static final String DURATION = "duration";

    @Override
    public Certificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Certificate.builder()
                .id(rs.getLong(ID))
                .name(rs.getString(NAME))
                .description(rs.getString(DESCRIPTION))
                .price(rs.getDouble(PRICE))
                .creationDate(rs.getTimestamp(CREATION_DATE).toLocalDateTime())
                .lastUpdateDate(rs.getTimestamp(UPDATE_DATE).toLocalDateTime())
                .state(rs.getInt(STATE))
                .duration(rs.getInt(DURATION))
                .build();
    }
}
