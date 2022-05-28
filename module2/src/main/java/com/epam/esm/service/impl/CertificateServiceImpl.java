package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.exception.CertificateNotFoundException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.TagService;
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
        Set<Tag> uniqueTags = new HashSet<>(certificate.getTags());
        List<Tag> tagsForCertificate = getOrSaveTags(uniqueTags);
        certificate.setTags(tagsForCertificate);

        return certificateDao.createCertificate(certificate);
    }

    private List<Tag> getOrSaveTags(Set<Tag> tags) {
        return tags.stream().
                map(elem -> tagDao.getTagByName(elem.getName()).orElseGet(() -> tagDao.createTag(elem)))
                .collect(Collectors.toList());
    }
}
