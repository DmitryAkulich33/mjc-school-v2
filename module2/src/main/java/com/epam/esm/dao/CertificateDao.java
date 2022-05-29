package com.epam.esm.dao;

import com.epam.esm.domain.Certificate;

import java.util.List;
import java.util.Optional;

public interface CertificateDao {
    Optional<Certificate> getCertificateById(Long id);

    void deleteCertificate(Long id);

    Certificate createCertificate(Certificate certificate);

    Certificate updateCertificate(Certificate certificate);

    List<Certificate> getCertificates(String tagName, String searchQuery, Boolean sortAsc, String sortField, Integer offset, Integer pageSize);

    List<Certificate> getCertificates(String tagName, String searchQuery, Boolean sortAsc, String sortField);

    List<Certificate> getCertificatesByTags(List<String> tagNames, Integer offset, Integer pageSize);
}
