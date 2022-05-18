package com.epam.esm.controller;

import com.epam.esm.domain.Certificate;
import com.epam.esm.model.dto.CertificateDto;
import com.epam.esm.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CertificateControllerImpl implements CertificateController {
    private final CertificateService certificateService;

    @Autowired
    public CertificateControllerImpl(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @Override
    public ResponseEntity<List<CertificateDto>> getAllCertificates(String tagName, String searchQuery, String sort) {
        return ResponseEntity.ok(certificateService.getAllCertificates(tagName, searchQuery, sort));
    }

    @Override
    public ResponseEntity<CertificateDto> getCertificateById(Long id) {
        return ResponseEntity.ok(certificateService.getCertificateById(id));
    }

    @Override
    public ResponseEntity<CertificateDto> deleteCertificate(Long id) {
        certificateService.deleteCertificate(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<CertificateDto> createCertificate(Certificate certificate) {
        return new ResponseEntity<>(certificateService.createCertificate(certificate), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<CertificateDto> updateCertificate(Certificate certificate, Long id) {
        return ResponseEntity.ok(certificateService.updateCertificate(certificate, id));
    }
}
