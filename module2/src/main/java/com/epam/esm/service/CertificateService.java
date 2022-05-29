package com.epam.esm.service;

import com.epam.esm.domain.Certificate;

import java.util.List;

public interface CertificateService {
    Certificate getCertificateById(Long id);

    void deleteCertificate(Long id);

    Certificate createCertificate(Certificate certificate);

    Certificate updatePartCertificate(Certificate certificate, Long id);

    Certificate updateCertificate(Certificate certificate, Long id);

    List<Certificate> getCertificates(String name, String search, String sort, Integer pageNumber, Integer pageSize);

    List<Certificate> getCertificatesByTags(List<String> tagNames, Integer pageNumber, Integer pageSize);
}
