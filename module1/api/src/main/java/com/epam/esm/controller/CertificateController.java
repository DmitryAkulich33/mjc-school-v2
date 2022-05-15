package com.epam.esm.controller;

import com.epam.esm.domain.Certificate;
import com.epam.esm.model.dto.CertificateDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/certificates")
public interface CertificateController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CertificateDto> getAllCertificates();

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CertificateDto> getCertificateById(@PathVariable Long id);

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CertificateDto> deleteCertificate(@PathVariable Long id);

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CertificateDto> createCertificate(@RequestBody Certificate certificate);

    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CertificateDto> updateCertificate(@RequestBody Certificate certificate, @PathVariable Long id);
}
