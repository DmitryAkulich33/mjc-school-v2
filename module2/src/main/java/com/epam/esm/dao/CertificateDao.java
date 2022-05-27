package com.epam.esm.dao;

import com.epam.esm.domain.Certificate;

import java.util.Optional;

public interface CertificateDao {
    Optional<Certificate> getCertificateById(Long id);
}