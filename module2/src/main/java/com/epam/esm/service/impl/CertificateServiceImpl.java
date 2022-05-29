package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.exception.CertificateNotFoundException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.TagService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CertificateServiceImpl implements CertificateService {
    private final CertificateDao certificateDao;
    private final TagService tagService;
    private final TagDao tagDao;

    private static Logger log = LogManager.getLogger(CertificateServiceImpl.class);

    @Autowired
    public CertificateServiceImpl(CertificateDao certificateDao, TagService tagService, TagDao tagDao) {
        this.certificateDao = certificateDao;
        this.tagService = tagService;
        this.tagDao = tagDao;
    }

    @Override
    public Certificate getCertificateById(Long id) {
        log.debug(String.format("Getting certificate with id: %d", id));
        Optional<Certificate> optionalCertificate = certificateDao.getCertificateById(id);
        return optionalCertificate.orElseThrow(() -> new CertificateNotFoundException("certificate.id.not.found", id));
    }

    @Transactional
    @Override
    public void deleteCertificate(Long id) {
        log.debug(String.format("Removing certificate with id %d", id));
        getCertificateById(id);
        certificateDao.deleteCertificate(id);
    }

    @Transactional
    @Override
    public Certificate createCertificate(Certificate certificate) {
        log.debug(String.format("Creation certificate.%s", certificate.toString()));
        List<Tag> tagsForCertificate = getOrSaveTags(certificate.getTags());
        certificate.setTags(tagsForCertificate);

        return certificateDao.createCertificate(certificate);
    }

    @Transactional
    @Override
    public Certificate updatePartCertificate(Certificate certificate, Long id) {
        log.debug(String.format("Service: update part certificate with id %d and certificate: %s", id, certificate.toString()));
        Certificate certificateToUpdate = getCertificateById(id);

        certificateToUpdate.setName(composeCertificateName(certificate, certificateToUpdate));
        certificateToUpdate.setDescription(composeCertificateDescription(certificate, certificateToUpdate));
        certificateToUpdate.setPrice(composeCertificatePrice(certificate, certificateToUpdate));
        certificateToUpdate.setDuration(composeCertificateDuration(certificate, certificateToUpdate));
        certificateToUpdate.setTags(composeCertificateTags(certificate, certificateToUpdate));

        return certificateDao.updateCertificate(certificateToUpdate);
    }

    private String composeCertificateName(Certificate certificate, Certificate certificateToUpdate) {
        String nameToUpdate = certificate.getName();
        return StringUtils.isBlank(nameToUpdate) ? certificateToUpdate.getName() : nameToUpdate;
    }

    private String composeCertificateDescription(Certificate certificate, Certificate certificateToUpdate) {
        String descriptionToUpdate = certificate.getDescription();
        return StringUtils.isBlank(descriptionToUpdate) ? certificateToUpdate.getDescription() : descriptionToUpdate;
    }

    private Double composeCertificatePrice(Certificate certificate, Certificate certificateToUpdate) {
        Double priceToUpdate = certificate.getPrice();
        return priceToUpdate == null ? certificateToUpdate.getPrice() : priceToUpdate;
    }

    private Integer composeCertificateDuration(Certificate certificate, Certificate certificateToUpdate) {
        Integer durationToUpdate = certificate.getDuration();
        return durationToUpdate == null ? certificateToUpdate.getDuration() : durationToUpdate;
    }

    private List<Tag> composeCertificateTags(Certificate certificate, Certificate certificateToUpdate) {
        List<Tag> tagsToUpdate = certificate.getTags();
        List<Tag> certificateTags = certificateToUpdate.getTags();
        if (tagsToUpdate != null && !tagsToUpdate.isEmpty()) {
            return getOrSaveTags(tagsToUpdate);
        }

        return certificateTags;
    }

    @Transactional
    @Override
    public Certificate updateCertificate(Certificate certificate, Long id) {
        log.debug(String.format("Service: update part certificate with id %d and certificate: %s", id, certificate.toString()));
        Certificate certificateToUpdate = getCertificateById(id);

        List<Tag> tagsForCertificate = getOrSaveTags(certificate.getTags());

        certificateToUpdate.setName(certificate.getName());
        certificateToUpdate.setDescription(certificate.getDescription());
        certificateToUpdate.setDuration(certificate.getDuration());
        certificateToUpdate.setPrice(certificate.getPrice());
        certificateToUpdate.setTags(tagsForCertificate);

        return certificateDao.updateCertificate(certificateToUpdate);
    }

    private List<Tag> getOrSaveTags(List<Tag> tags) {
        Set<Tag> uniqueTags = new HashSet<>(tags);
        return uniqueTags.stream().
                map(elem -> tagDao.getTagByName(elem.getName()).orElseGet(() -> tagDao.createTag(elem)))
                .collect(Collectors.toList());
    }
}
