package com.epam.esm.dao;

import com.epam.esm.dao.builder.CertificateQueryBuilder;
import com.epam.esm.domain.Certificate;
import com.epam.esm.exception.CertificateDaoException;
import com.epam.esm.exception.CertificateDuplicateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class CertificateDaoImpl implements CertificateDao {
    private static final String ADD_CERTIFICATE = "INSERT INTO certificate (name, description, price, " +
            "creation_date, update_date, state, duration) VALUES(?,?,?,?,?,?,?)";
    private static final String FIND_DISTINCT_FROM_CERTIFICATES = "SELECT DISTINCT certificate.id, certificate.name, description, " +
            "price, creation_date, update_date, certificate.state, duration FROM certificate";
    private static final String FIND_CERTIFICATE_BY_ID = "SELECT id, name, description, price, creation_date, " +
            "update_date, state, duration FROM certificate WHERE id=? AND state=0";
    private static final String DELETE_CERTIFICATE_BY_ID = "UPDATE certificate SET state=1 WHERE id=?";
    private static final Integer STATE = 0;
    private static final String UPDATE_CERTIFICATE = "UPDATE certificate SET name=?, description=?, " +
            "price=?, update_date=?, duration=? WHERE id=?";
    private static final String CERTIFICATE_UNLOCK = " certificate.state=0";
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Certificate> rowMapper;
    private final CertificateQueryBuilder certificateQueryBuilder;

    @Autowired
    public CertificateDaoImpl(JdbcTemplate jdbcTemplate, RowMapper<Certificate> rowMapper, CertificateQueryBuilder certificateQueryBuilder) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
        this.certificateQueryBuilder = certificateQueryBuilder;
    }

    @Override
    public Optional<Certificate> getCertificateById(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_CERTIFICATE_BY_ID, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteCertificate(Long id) {
        try {
            jdbcTemplate.update(DELETE_CERTIFICATE_BY_ID, id);
        } catch (DataAccessException e) {
            throw new CertificateDaoException("server.error");
        }
    }

    @Override
    public Certificate createCertificate(Certificate certificate) {
        LocalDateTime creationDate = LocalDateTime.now();
        Timestamp parsedDate = Timestamp.valueOf(creationDate);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(ADD_CERTIFICATE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, certificate.getName());
                ps.setString(2, certificate.getDescription());
                ps.setDouble(3, certificate.getPrice());
                ps.setTimestamp(4, parsedDate);
                ps.setTimestamp(5, parsedDate);
                ps.setInt(6, STATE);
                ps.setInt(7, certificate.getDuration());
                return ps;
            }, keyHolder);
        } catch (DuplicateKeyException e) {
            throw new CertificateDuplicateException("certificate.exists", certificate.getName());
        } catch (DataAccessException e) {
            throw new CertificateDaoException("server.error");
        }

        Long tagId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        certificate.setId(tagId);
        certificate.setState(STATE);
        certificate.setCreationDate(creationDate);
        certificate.setLastUpdateDate(creationDate);

        return certificate;
    }

    @Override
    public Certificate updateCertificate(Certificate certificate) {
        Long id = certificate.getId();
        String name = certificate.getName();
        String description = certificate.getDescription();
        double price = certificate.getPrice();
        LocalDateTime updateDate = LocalDateTime.now();
        int duration = certificate.getDuration();
        try {
            jdbcTemplate.update(UPDATE_CERTIFICATE, name, description, price, updateDate, duration, id);
        } catch (DataAccessException e) {
            throw new CertificateDaoException("server.error");
        }
        certificate.setLastUpdateDate(updateDate);

        return certificate;
    }

    @Override
    public List<Certificate> getCertificates(String tagName, String searchQuery, String sort) {
        StringBuilder certificateQuery = new StringBuilder(FIND_DISTINCT_FROM_CERTIFICATES)
                .append(certificateQueryBuilder.buildTagNameQuery(tagName))
                .append(certificateQueryBuilder.buildSearchQuery(searchQuery))
                .append(CERTIFICATE_UNLOCK)
                .append(certificateQueryBuilder.buildSortQuery(sort));
        try {
            return jdbcTemplate.query(certificateQuery.toString(), rowMapper);
        } catch (DataAccessException e) {
            throw new CertificateDaoException("server.error");
        }
    }
}
