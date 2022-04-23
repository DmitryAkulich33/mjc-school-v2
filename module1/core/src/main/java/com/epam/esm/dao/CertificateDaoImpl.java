package com.epam.esm.dao;

import com.epam.esm.domain.Certificate;
import com.epam.esm.exception.CertificateDaoException;
import com.epam.esm.exception.CertificateDuplicateException;
import com.epam.esm.exception.TagDaoException;
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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class CertificateDaoImpl implements CertificateDao {
    private static final String ADD_CERTIFICATE = "INSERT INTO certificate (name, description, price, " +
            "creation_date, state, duration) VALUES(?,?,?,?,?,?)";
    private static final String FIND_DISTINCT_FROM_CERTIFICATES = "SELECT DISTINCT id, name, description, price, " +
            "creation_date, update_date, state, duration FROM certificate";
    private static final String FIND_CERTIFICATE_BY_ID = "SELECT id, name, description, price, creation_date, " +
            "update_date, state, duration FROM certificate WHERE id=? AND state=0";
    private static final String DELETE_CERTIFICATE_BY_ID = "UPDATE certificate SET state=1 WHERE id=?";
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Certificate> rowMapper;
    private static final Integer STATE = 0;

    @Autowired
    public CertificateDaoImpl(JdbcTemplate jdbcTemplate, RowMapper<Certificate> rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public List<Certificate> getAllCertificates() {
        return null;
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
            throw new CertificateDaoException("Server problems");
        }
    }

    @Override
    public Certificate createCertificate(Certificate certificate) {
        LocalDateTime creationDate = LocalDateTime.now();
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(ADD_CERTIFICATE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, certificate.getName());
                ps.setString(2, certificate.getDescription());
                ps.setDouble(3, certificate.getPrice());
                ps.setTimestamp(4, Timestamp.valueOf(creationDate));
                ps.setInt(5, STATE);
                ps.setInt(6, certificate.getDuration());
                return ps;
            }, keyHolder);
        } catch (DuplicateKeyException e) {
            throw new CertificateDuplicateException("Certificate is already exists");
        } catch (DataAccessException e) {
            throw new CertificateDaoException("Server problems");
        }

        Long idTag = Objects.requireNonNull(keyHolder.getKey()).longValue();
        certificate.setId(idTag);
        certificate.setState(STATE);
        certificate.setCreationDate(creationDate);
        return certificate;
    }
}
