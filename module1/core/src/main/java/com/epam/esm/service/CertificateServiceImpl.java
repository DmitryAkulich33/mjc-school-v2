package com.epam.esm.service;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.exception.CertificateNotFoundException;
import com.epam.esm.model.dto.CertificateDto;
import com.epam.esm.service.mapper.CertificateDtoMapper;
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
    private final TagDao tagDao;
    private final CertificateDtoMapper certificateDtoMapper;

    @Autowired
    public CertificateServiceImpl(CertificateDao certificateDao, TagDao tagDao, CertificateDtoMapper certificateDtoMapper) {
        this.certificateDao = certificateDao;
        this.tagDao = tagDao;
        this.certificateDtoMapper = certificateDtoMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificateDto> getAllCertificates(String name, String searchQuery, String sort) {
        List<Certificate> certificates = certificateDao.getCertificates(name, searchQuery, sort);
        certificates.forEach(certificate -> certificate.setTags(tagDao.getTagsFromCertificate(certificate.getId())));
        return certificateDtoMapper.toCertificateDtoList(certificates);
    }

    @Override
    @Transactional(readOnly = true)
    public CertificateDto getCertificateById(Long id) {
        Optional<Certificate> optionalCertificate = certificateDao.getCertificateById(id);
        Certificate certificate = optionalCertificate.orElseThrow(() ->
                new CertificateNotFoundException("Certificate with id " + id + " isn't found"));
        certificate.setTags(tagDao.getTagsFromCertificate(id));

        return certificateDtoMapper.toCertificateDto(certificate);
    }

    @Override
    public void deleteCertificate(Long id) {
        getCertificateById(id);
        certificateDao.deleteCertificate(id);
    }

    @Override
    @Transactional
    public CertificateDto createCertificate(Certificate certificate) {
        Certificate createdCertificate = certificateDao.createCertificate(certificate);
        Set<Tag> uniqueTags = new HashSet<>(certificate.getTags());
        Long id = createdCertificate.getId();
        List<Tag> certificateTags = getOrSaveTags(uniqueTags);
        certificateTags.forEach(elem -> tagDao.createTagCertificate(elem.getId(), id));
        certificate.setTags(certificateTags);

        return certificateDtoMapper.toCertificateDto(certificate);
    }

    @Override
    @Transactional
    public CertificateDto updateCertificate(Certificate certificate, Long id) {
        Optional<Certificate> optionalCertificate = certificateDao.getCertificateById(id);
        Certificate certificateToUpdate = optionalCertificate.orElseThrow(() ->
                new CertificateNotFoundException("Certificate with id " + id + " isn't found"));

        certificateToUpdate.setName(composeCertificateName(certificate, certificateToUpdate));
        certificateToUpdate.setDescription(composeCertificateDescription(certificate, certificateToUpdate));
        certificateToUpdate.setPrice(composeCertificatePrice(certificate, certificateToUpdate));
        certificateToUpdate.setDuration(composeCertificateDuration(certificate, certificateToUpdate));
        certificateToUpdate.setTags(composeCertificateTags(certificate, id));
        certificateDao.updateCertificate(certificateToUpdate);

        return certificateDtoMapper.toCertificateDto(certificateToUpdate);
    }

    private String composeCertificateName(Certificate certificate, Certificate certificateToUpdate) {
        String nameToUpdate = certificate.getName();
        return nameToUpdate.isBlank() ? certificateToUpdate.getName() : nameToUpdate;
    }

    private String composeCertificateDescription(Certificate certificate, Certificate certificateToUpdate) {
        String descriptionToUpdate = certificate.getDescription();
        return descriptionToUpdate.isBlank() ? certificateToUpdate.getDescription() : descriptionToUpdate;
    }

    private Double composeCertificatePrice(Certificate certificate, Certificate certificateToUpdate) {
        Double priceToUpdate = certificate.getPrice();
        return priceToUpdate == null ? certificateToUpdate.getPrice() : priceToUpdate;
    }

    private Integer composeCertificateDuration(Certificate certificate, Certificate certificateToUpdate) {
        Integer durationToUpdate = certificate.getDuration();
        return durationToUpdate == null ? certificateToUpdate.getDuration() : durationToUpdate;
    }

    private List<Tag> composeCertificateTags(Certificate certificate, Long certificateId) {
        List<Tag> tagsToUpdate = certificate.getTags();
        List<Tag> certificateTags = tagDao.getTagsFromCertificate(certificateId);
        if (tagsToUpdate != null && !tagsToUpdate.isEmpty()) {

            Set<Tag> uniqueTags = new HashSet<>(tagsToUpdate);
            List<Tag> uniqueTagsToUpdate = getOrSaveTags(uniqueTags);
            saveNewTagsInCertificate(uniqueTagsToUpdate, certificateTags, certificateId);
            deleteUnusedTagInCertificate(uniqueTagsToUpdate, certificateTags, certificateId);

            return uniqueTagsToUpdate;
        }
        return certificateTags;
    }

    private void saveNewTagsInCertificate(List<Tag> uniqueTagsToUpdate, List<Tag> certificateTags, Long certificateId) {
        getNewTagsForCertificate(uniqueTagsToUpdate, certificateTags).forEach(elem -> tagDao.createTagCertificate(elem.getId(), certificateId));
    }

    private List<Tag> getNewTagsForCertificate(List<Tag> uniqueTagsToUpdate, List<Tag> certificateTags) {
        return uniqueTagsToUpdate.stream()
                .filter(elem -> !isTagBelongToCertificate(certificateTags, elem.getId()))
                .collect(Collectors.toList());
    }

    private void deleteUnusedTagInCertificate(List<Tag> uniqueTagsToUpdate, List<Tag> certificateTags, Long certificateId) {
        certificateTags.forEach(elem -> {
            if (!uniqueTagsToUpdate.contains(elem)) {
                tagDao.deleteTagCertificate(elem.getId(), certificateId);
            }
        });
    }

    private List<Tag> getOrSaveTags(Set<Tag> tags) {
        return tags.stream().
                map(elem -> tagDao.getTagByName(elem.getName()).orElseGet(() -> tagDao.createTag(elem)))
                .collect(Collectors.toList());
    }

    private boolean isTagBelongToCertificate(List<Tag> tags, Long tagId) {
        return tags.stream().anyMatch(tag -> tag.getId().equals(tagId));
    }
}
