package com.epam.esm.dao;

import com.epam.esm.domain.Certificate;

import java.util.List;
import java.util.Optional;

public interface CertificateDao {

    Optional<Certificate> getCertificateById(Long id);

    void deleteCertificate(Long id);

    void createCertificate(Certificate certificate);

    void updateCertificate(Certificate certificate);

    List<Certificate> getCertificates(String name, String search, String sort);
}
