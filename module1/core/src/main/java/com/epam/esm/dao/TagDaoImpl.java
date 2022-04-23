package com.epam.esm.dao;

import com.epam.esm.domain.Tag;
import com.epam.esm.exception.TagDaoException;
import com.epam.esm.exception.TagDuplicateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class TagDaoImpl implements TagDao {
    private static final String FIND_ALL_TAGS = "SELECT id, name, state FROM tag;";
    private static final String FIND_TAG_BY_ID = "SELECT id, name, state FROM tag WHERE id=?;";
    private static final String ADD_TAG = "INSERT INTO tag (name) VALUES(?)";
    private static final String DELETE_TAG_BY_ID = "UPDATE tag SET state=1 WHERE id=?";
    private static final String FIND_TAGS_FROM_CERTIFICATE = "SELECT id, name, state FROM tag " +
            "JOIN tag_certificate ON tag.id=tag_certificate.tag_id WHERE certificate_id=? and tag.state=0";
    private static final Integer DEFAULT_STATE = 0;
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Tag> rowMapper;

    @Autowired
    public TagDaoImpl(JdbcTemplate jdbcTemplate, RowMapper<Tag> rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    public List<Tag> getAllTags() {
        return jdbcTemplate.query(FIND_ALL_TAGS, rowMapper);
    }

    @Override
    public Optional<Tag> getTagById(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_TAG_BY_ID, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Tag createTag(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(ADD_TAG, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, tag.getName());
                return ps;
            }, keyHolder);
        } catch (DuplicateKeyException e) {
            throw new TagDuplicateException("Tag with this name is already exist");
        } catch (DataAccessException e) {
            throw new TagDaoException("Server problems");
        }

        Long idTag = Objects.requireNonNull(keyHolder.getKey()).longValue();
        tag.setId(idTag);
        tag.setState(DEFAULT_STATE);
        return tag;
    }

    @Override
    public void deleteTag(Long id) {
        try {
            jdbcTemplate.update(DELETE_TAG_BY_ID, id);
        } catch (DataAccessException e) {
            throw new TagDaoException("Server problems");
        }
    }

    @Override
    public List<Tag> getTagsFromCertificate(Long id) {
        try {
            return jdbcTemplate.query(FIND_TAGS_FROM_CERTIFICATE, rowMapper, id);
        } catch (DataAccessException e) {
            throw new TagDaoException("Server problems");
        }
    }
}
