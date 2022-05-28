package com.epam.esm.controller;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.model.CertificateModel;
import com.epam.esm.service.CertificateService;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Validated
@RestController
@RequestMapping(value = "/api/v1/certificates")
public class CertificateController {
    private final CertificateService certificateService;
    private static Logger log = LogManager.getLogger(TagController.class);

    @Autowired
    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @JsonView(CertificateModel.Views.V1.class)
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CertificateModel> getCertificateById(@PathVariable("id") @NotNull @Positive Long id) {
        log.debug("Getting certificate with id: " + id);
        Certificate certificate = certificateService.getCertificateById(id);
        CertificateModel certificateModel = CertificateModel.createForm(certificate);

        return new ResponseEntity<>(certificateModel, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Certificate> deleteCertificate(@PathVariable("id") @NotNull @Positive Long id) {
        certificateService.deleteCertificate(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
