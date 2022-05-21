package com.epam.esm.dao;

import com.epam.esm.config.TestConfig;
import com.epam.esm.domain.Certificate;
import com.epam.esm.exception.CertificateDaoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@SqlGroup({
        @Sql(scripts = "/drop_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
        @Sql(scripts = "/create_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
        @Sql(scripts = "/init_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class CertificateDaoImplTest {
    private Certificate certificate1;
    private Certificate certificate2;
    private Certificate certificate3;

    @Autowired
    private CertificateDao certificateDao;

    @BeforeEach
    public void setUp() {
        certificate1 = Certificate.builder()
                .id(1L)
                .name("Certificate for one purchase")
                .description("Certificate for one going to the shop")
                .price(50.0)
                .creationDate(LocalDateTime.parse("2020-10-22T11:45:11"))
                .lastUpdateDate(LocalDateTime.parse("2020-10-22T11:50:50"))
                .state(0)
                .duration(360)
                .build();

        certificate2 = Certificate.builder()
                .id(2L)
                .name("Certificate for dinner in a restaurant")
                .description("Food and drink without check limit at Viet Express")
                .price(100.0)
                .creationDate(LocalDateTime.parse("2020-11-22T12:45:11"))
                .lastUpdateDate(LocalDateTime.parse("2020-11-22T12:55:55"))
                .state(0)
                .duration(100)
                .build();

        certificate3 = Certificate.builder()
                .name("SPA certificate")
                .description("Romantic SPA date for two any day")
                .price(100.0)
                .creationDate(LocalDateTime.parse("2020-11-22T12:45:11"))
                .lastUpdateDate(LocalDateTime.parse("2020-12-02T10:15:33"))
                .state(0)
                .duration(100)
                .build();
    }

    @Test
    public void testGetCertificateById() {
        Optional<Certificate> expected = Optional.of(certificate1);
        Optional<Certificate> actual = certificateDao.getCertificateById(1L);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificateById_CertificateNotFound() {
        Optional<Certificate> expected = Optional.empty();
        Optional<Certificate> actual = certificateDao.getCertificateById(100L);

        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteCertificate() {
        certificate2.setState(1);
        List<Certificate> expected = new ArrayList<>(Collections.singletonList(certificate1));

        certificateDao.deleteCertificate(2L);
        List<Certificate> actual = certificateDao.getCertificates(null, null, null);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificates_WithoutParams() {
        List<Certificate> expected = new ArrayList<>(Arrays.asList(certificate1, certificate2));

        List<Certificate> actual = certificateDao.getCertificates(null, null, null);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificates_WithAllParams_sortAsc() {
        List<Certificate> expected = new ArrayList<>(Arrays.asList(certificate1, certificate2));

        List<Certificate> actual = certificateDao.getCertificates("food", "Certificate", "creation_date_asc");

        assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificates_WithAllParams_sortDesc() {
        List<Certificate> expected = new ArrayList<>(Arrays.asList(certificate2, certificate1));

        List<Certificate> actual = certificateDao.getCertificates("food", "Certificate", "creation_date_desc");

        assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificates_sortAsc() {
        List<Certificate> expected = new ArrayList<>(Arrays.asList(certificate1, certificate2));

        List<Certificate> actual = certificateDao.getCertificates(null, null, "creation_date_asc");

        assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificates_sortDesc() {
        List<Certificate> expected = new ArrayList<>(Arrays.asList(certificate2, certificate1));

        List<Certificate> actual = certificateDao.getCertificates(null, null, "creation_date_desc");

        assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificates_query_sortAsc() {
        List<Certificate> expected = new ArrayList<>(Arrays.asList(certificate1, certificate2));

        List<Certificate> actual = certificateDao.getCertificates(null, "Certificate", "creation_date_asc");

        assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificates_query_sortDesc() {
        List<Certificate> expected = new ArrayList<>(Arrays.asList(certificate2, certificate1));

        List<Certificate> actual = certificateDao.getCertificates(null, "Certificate", "creation_date_desc");

        assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificates_tagName_sortAsc() {
        List<Certificate> expected = new ArrayList<>(Arrays.asList(certificate1, certificate2));

        List<Certificate> actual = certificateDao.getCertificates("food", null, "creation_date_asc");

        assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificates_tagName_sortDesc() {
        List<Certificate> expected = new ArrayList<>(Arrays.asList(certificate2, certificate1));

        List<Certificate> actual = certificateDao.getCertificates("food", null, "creation_date_desc");

        assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificates_query() {
        List<Certificate> expected = new ArrayList<>(Collections.singletonList(certificate2));

        List<Certificate> actual = certificateDao.getCertificates(null, "dinner", null);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificates_tagName_query() {
        List<Certificate> expected = new ArrayList<>(Collections.singletonList(certificate2));

        List<Certificate> actual = certificateDao.getCertificates("food", "dinner", null);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificates_tagName() {
        List<Certificate> expected = new ArrayList<>(Collections.singletonList(certificate1));

        List<Certificate> actual = certificateDao.getCertificates("delivery", null, null);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificates_CertificateDaoException() {
        assertThrows(CertificateDaoException.class, () -> {
            certificateDao.getCertificates(null, null, "wrong_sort");
        });
    }

    @Test
    public void testUpdateCertificate_duration_price() {
        certificate2.setPrice(150.0);
        certificate2.setDuration(360);
        certificateDao.updateCertificate(certificate2);

        Certificate actual = certificateDao.getCertificateById(2L).orElse(new Certificate());

        Assertions.assertEquals(150, actual.getPrice());
        Assertions.assertEquals(360, actual.getDuration());
    }

    @Test
    public void testUpdateCertificate_name() {
        certificate2.setName("New name");
        certificateDao.updateCertificate(certificate2);

        Certificate actual = certificateDao.getCertificateById(2L).orElse(new Certificate());

        Assertions.assertEquals("New name", actual.getName());
    }

    @Test
    public void testCreateCertificate() {
        certificateDao.createCertificate(certificate3);

        Certificate actual = certificateDao.getCertificateById(3L).orElse(new Certificate());

        Assertions.assertEquals(3L, actual.getId());
    }
}