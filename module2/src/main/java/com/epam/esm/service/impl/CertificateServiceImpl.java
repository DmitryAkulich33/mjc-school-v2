package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.exception.CertificateNotFoundException;
import com.epam.esm.service.CertificateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CertificateServiceImpl implements CertificateService {
    private final CertificateDao certificateDao;

    private static Logger log = LogManager.getLogger(CertificateServiceImpl.class);

    @Autowired
    public CertificateServiceImpl(CertificateDao certificateDao) {
        this.certificateDao = certificateDao;
    }

    @Override
    public Certificate getCertificateById(Long id) {
        log.debug(String.format("Getting certificate with id: %d", id));
        Optional<Certificate> optionalCertificate = certificateDao.getCertificateById(id);
        return optionalCertificate.orElseThrow(() -> new CertificateNotFoundException("certificate.id.not.found", id));
    }

    @Override
    public void deleteCertificate(Long id) {
        log.debug(String.format("Removing certificate with id %d", id));
        getCertificateById(id);
        certificateDao.deleteCertificate(id);
    }
}
