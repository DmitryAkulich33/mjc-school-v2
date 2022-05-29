package com.epam.esm.controller;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.model.CertificateModel;
import com.epam.esm.domain.model.CreateCertificateModel;
import com.epam.esm.domain.model.UpdateCertificateModel;
import com.epam.esm.domain.model.UpdatePartCertificateModel;
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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

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

    @JsonView(CertificateModel.Views.V1.class)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CertificateModel> createCertificate(@Valid @RequestBody @JsonView(CreateCertificateModel.Views.V1.class)
                                                                     CreateCertificateModel createCertificateView) {
        Certificate certificate = CreateCertificateModel.createForm(createCertificateView);
        Certificate createdCertificate = certificateService.createCertificate(certificate);
        CertificateModel certificateModel = CertificateModel.createForm(createdCertificate);

        return new ResponseEntity<>(certificateModel, HttpStatus.CREATED);
    }

    @JsonView(CertificateModel.Views.V1.class)
    @PatchMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CertificateModel> updatePartCertificate(
            @Valid @RequestBody @JsonView(UpdatePartCertificateModel.Views.V1.class) UpdatePartCertificateModel updatePartCertificateModel,
            @PathVariable @NotNull @Positive Long id) {
        Certificate certificateFromQuery = UpdatePartCertificateModel.createForm(updatePartCertificateModel);
        Certificate certificateToUpdate = certificateService.updatePartCertificate(certificateFromQuery, id);
        CertificateModel certificateView = CertificateModel.createForm(certificateToUpdate);

        return new ResponseEntity<>(certificateView, HttpStatus.OK);
    }

    @JsonView(CertificateModel.Views.V1.class)
    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CertificateModel> updateCertificate(
            @Valid @RequestBody @JsonView(UpdateCertificateModel.Views.V1.class) UpdateCertificateModel updateCertificateModel,
                                                             @PathVariable("id") @NotNull @Positive Long id) {
        Certificate certificateFromQuery = UpdateCertificateModel.createForm(updateCertificateModel);
        Certificate certificateToUpdate = certificateService.updateCertificate(certificateFromQuery, id);
        CertificateModel certificateView = CertificateModel.createForm(certificateToUpdate);

        return new ResponseEntity<>(certificateView, HttpStatus.OK);
    }

    @JsonView(CertificateModel.Views.V1.class)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CertificateModel>> getCertificates(@RequestParam(name = "tagName", required = false) String tagName,
                                                                  @RequestParam(name = "searchQuery", required = false) String searchQuery,
                                                                  @RequestParam(name = "sort", required = false) String sort,
                                                                  @RequestParam(required = false) @Positive Integer pageNumber,
                                                                  @RequestParam(required = false) @Positive Integer pageSize) {
        List<Certificate> certificates = certificateService.getCertificates(tagName, searchQuery, sort, pageNumber, pageSize);
        List<CertificateModel> certificateModels = CertificateModel.createListForm(certificates);

        return new ResponseEntity<>(certificateModels, HttpStatus.OK);
    }
}
