package com.epam.esm.service;

import com.epam.esm.domain.Certificate;

public interface CertificateService {
    Certificate getCertificateById(Long id);
    void deleteCertificate(Long id);
    Certificate createCertificate(Certificate certificate);
}
