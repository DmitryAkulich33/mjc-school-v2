package com.epam.esm.dao.mapper;

import com.epam.esm.domain.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TagRowMapper implements RowMapper<Tag> {
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String STATE = "state";

    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Tag.builder()
                .id(rs.getLong(ID))
                .name(rs.getString(NAME))
                .state(rs.getInt(STATE))
                .build();
    }
}
