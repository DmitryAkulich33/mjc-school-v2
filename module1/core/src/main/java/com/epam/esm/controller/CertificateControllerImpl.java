package com.epam.esm.controller;

import com.epam.esm.domain.Certificate;
import com.epam.esm.model.dto.CertificateDto;
import com.epam.esm.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CertificateControllerImpl implements CertificateController {
    private final CertificateService certificateService;

    @Autowired
    public CertificateControllerImpl(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @Override
    public ResponseEntity<CertificateDto> getAllCertificates() {
        return null;
    }

    @Override
    public ResponseEntity<CertificateDto> getCertificateById(Long id) {
        return ResponseEntity.ok(certificateService.getCertificateById(id));
    }

    @Override
    public ResponseEntity<CertificateDto> deleteCertificate(Long id) {
        certificateService.deleteCertificate(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<CertificateDto> createCertificate(Certificate certificate) {
        return ResponseEntity.ok(certificateService.createCertificate(certificate));
    }
}
