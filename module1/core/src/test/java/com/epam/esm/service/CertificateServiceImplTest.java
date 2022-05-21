package com.epam.esm.service;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.exception.CertificateNotFoundException;
import com.epam.esm.model.dto.CertificateDto;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.service.mapper.CertificateDtoMapper;
import com.epam.esm.service.validator.CertificateValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CertificateServiceImplTest {
    @Mock
    private CertificateDao mockCertificateDao;
    @Mock
    private CertificateValidator mockCertificateValidator;
    @Mock
    private CertificateDtoMapper mockCertificateDtoMapper;
    @Mock
    private TagDao mockTagDao;

    private Certificate certificate = new Certificate();
    private List<Tag> tags = new ArrayList<>();
    private List<TagDto> tagsDto = new ArrayList<>();
    private CertificateDto certificateDto = new CertificateDto();
    private List<Certificate> certificates = new ArrayList<>();
    private List<CertificateDto> certificatesDto = new ArrayList<>();

    @InjectMocks
    private CertificateServiceImpl certificateServiceImpl;

    @BeforeEach
    void setup() {
        certificate.setTags(tags);
        certificateDto.setTags(tagsDto);
    }

    @Test
    public void testGetCertificateById() {
        when(mockCertificateDao.getCertificateById(anyLong())).thenReturn(Optional.of(certificate));
        when(mockCertificateDtoMapper.toCertificateDto(certificate)).thenReturn(certificateDto);
        when(mockTagDao.getTagsFromCertificate(anyLong())).thenReturn(tags);

        CertificateDto actual = certificateServiceImpl.getCertificateById(anyLong());

        assertEquals(certificateDto, actual);
    }

    @Test
    public void testGetCertificateById_CertificateNotFoundException() {
        when(mockCertificateDao.getCertificateById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CertificateNotFoundException.class, () -> {
            certificateServiceImpl.getCertificateById(anyLong());
        });
    }

    @Test
    public void testDeleteCertificateById() {
        when(mockCertificateDao.getCertificateById(anyLong())).thenReturn(Optional.of(certificate));

        certificateServiceImpl.deleteCertificate(anyLong());

        verify(mockCertificateDao).getCertificateById(anyLong());
        verify(mockCertificateDao).deleteCertificate(anyLong());
    }

    @Test
    public void testDeleteCertificateById_CertificateNotFoundException() {
        when(mockCertificateDao.getCertificateById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CertificateNotFoundException.class, () -> {
            certificateServiceImpl.deleteCertificate(anyLong());
            ;
        });
    }

    @Test
    public void testGetAllCertificates () {
        when(mockCertificateDao.getCertificates(anyString(), anyString(), anyString())).thenReturn(certificates);
        when(mockCertificateDtoMapper.toCertificateDtoList(certificates)).thenReturn(certificatesDto);

        List<CertificateDto> actual = certificateServiceImpl.getAllCertificates(anyString(), anyString(), anyString());

        assertEquals(certificatesDto, actual);
    }

    @Test
    public void testCreateCertificate() {
        when(mockCertificateDtoMapper.toCertificateDto(certificate)).thenReturn(certificateDto);

        CertificateDto actual = certificateServiceImpl.createCertificate(certificate);

        assertEquals(certificateDto, actual);
        verify(mockCertificateValidator).validateCertificateToCreate(certificate);
    }

    @Test
    public void testUpdateCertificate() {
        when(mockCertificateDao.getCertificateById(anyLong())).thenReturn(Optional.of(certificate));
        when(mockCertificateDtoMapper.toCertificateDto(certificate)).thenReturn(certificateDto);

        CertificateDto actual = certificateServiceImpl.updateCertificate(certificate, anyLong());

        assertEquals(certificateDto, actual);
        verify(mockCertificateValidator).validateCertificateToUpdate(certificate);
    }
}