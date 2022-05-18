package com.epam.esm.service.validator;

import com.epam.esm.domain.Certificate;

public interface CertificateValidator {
    void validateCertificateToCreate(Certificate certificate);

    void validateCertificateToUpdate(Certificate certificate);
}
