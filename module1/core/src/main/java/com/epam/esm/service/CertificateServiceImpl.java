package com.epam.esm.service;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.exception.CertificateNotFoundException;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.model.dto.CertificateDto;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.service.mapper.CertificateDtoMapper;
import com.epam.esm.service.mapper.TagDtoMapper;
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
    private final CertificateDtoMapper certificateMapper;
    private final TagDtoMapper tagDtoMapper;

    @Autowired
    public CertificateServiceImpl(CertificateDao certificateDao, TagDao tagDao, CertificateDtoMapper certificateMapper,
                                  TagDtoMapper tagDtoMapper) {
        this.certificateDao = certificateDao;
        this.tagDao = tagDao;
        this.certificateMapper = certificateMapper;
        this.tagDtoMapper = tagDtoMapper;
    }

    @Override
    public List<CertificateDto> getAllCertificates() {
        return null;
    }

    @Override
    public CertificateDto getCertificateById(Long id) {
        Optional<Certificate> optionalCertificate = certificateDao.getCertificateById(id);
        CertificateDto certificateDto = certificateMapper.toCertificateDto(optionalCertificate.orElseThrow(() ->
                new CertificateNotFoundException("Certificate with id " + id + " isn't found")));
        List<TagDto> tagsDto = tagDtoMapper.toTagDtoList(tagDao.getTagsFromCertificate(id));
        certificateDto.setTags(tagsDto);
        return certificateDto;
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

        return certificateMapper.toCertificateDto(certificate);
    }

    private List<Tag> getOrSaveTags(Set<Tag> tags) {
        return tags.stream().
                map(elem -> tagDao.getTagByName(elem.getName()).orElseGet(() -> tagDao.createTag(elem)))
                .collect(Collectors.toList());
    }

}
