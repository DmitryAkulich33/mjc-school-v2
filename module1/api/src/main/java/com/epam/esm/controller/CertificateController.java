package com.epam.esm.controller;

import com.epam.esm.domain.Certificate;
import com.epam.esm.model.dto.CertificateDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

@Validated
@RequestMapping(value = "/certificates")
public interface CertificateController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<CertificateDto>> getAllCertificates(@RequestParam(name = "tagName", required = false) String tagName,
                                                            @RequestParam(name = "searchQuery", required = false) String searchQuery,
                                                            @RequestParam(name = "sort", required = false) String sort);

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CertificateDto> getCertificateById(@PathVariable("id") @Positive Long id);

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CertificateDto> deleteCertificate(@PathVariable("id") @Positive Long id);

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CertificateDto> createCertificate(@RequestBody Certificate certificate);

    @PatchMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CertificateDto> updateCertificate(@RequestBody Certificate certificate, @PathVariable("id") @Positive Long id);
}
