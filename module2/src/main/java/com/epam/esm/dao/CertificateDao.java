package com.epam.esm.dao;

import com.epam.esm.domain.Certificate;

import java.util.Optional;

public interface CertificateDao {
    Optional<Certificate> getCertificateById(Long id);
    void deleteCertificate(Long id);
    Certificate createCertificate(Certificate certificate);
    Certificate updateCertificate(Certificate certificate);
}
