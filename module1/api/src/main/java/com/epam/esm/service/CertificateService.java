package com.epam.esm.service;

import com.epam.esm.domain.Certificate;
import com.epam.esm.model.dto.CertificateDto;

import java.util.List;

public interface CertificateService {
    List<CertificateDto> getAllCertificates();
    CertificateDto getCertificateById(Long id);
    void deleteCertificate(Long id);
    CertificateDto createCertificate(Certificate certificate);
}
