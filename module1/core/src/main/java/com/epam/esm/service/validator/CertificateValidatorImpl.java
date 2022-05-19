package com.epam.esm.service.validator;

import com.epam.esm.domain.Certificate;
import com.epam.esm.exception.CertificateValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CertificateValidatorImpl implements CertificateValidator {
    private static final String CERTIFICATE_NAME_REGEX = "^([A-Z].{0,99})$";
    private static final String CERTIFICATE_DESCRIPTION_REGEX = "^([A-Z].{0,299})$";
    private final ValidatorHelper validatorHelper;
    private final TagValidator tagValidator;

    @Autowired
    public CertificateValidatorImpl(ValidatorHelper validatorHelper, TagValidator tagValidator) {
        this.validatorHelper = validatorHelper;
        this.tagValidator = tagValidator;
    }

    @Override
    public void validateCertificateToCreate(Certificate certificate) {
        validateCertificateNameToCreate(certificate.getName());
        validateCertificateDescriptionToCreate(certificate.getDescription());
        validateCertificatePriceToCreate(certificate.getPrice());
        validateCertificateDurationToCreate(certificate.getDuration());
        tagValidator.validateTagsToCreateCertificate(certificate.getTags());
    }

    @Override
    public void validateCertificateToUpdate(Certificate certificate) {
        validateCertificateNameToUpdate(certificate.getName());
        validateCertificateDescriptionToUpdate(certificate.getDescription());
        validateCertificatePriceToUpdate(certificate.getPrice());
        validateCertificateDurationToUpdate(certificate.getDuration());
        tagValidator.validateTagsToUpdateCertificate(certificate.getTags());
    }

    private void validateCertificateNameToCreate(String name) {
        if (name != null && validatorHelper.validateStringParameter(CERTIFICATE_NAME_REGEX, name)) {
            return;
        }
        throw new CertificateValidatorException("certificate.name.not.valid", name);
    }

    private void validateCertificateNameToUpdate(String name) {
        if (name != null && !validatorHelper.validateStringParameter(CERTIFICATE_NAME_REGEX, name)) {
            throw new CertificateValidatorException("certificate.name.not.valid", name);
        }
    }

    private void validateCertificateDescriptionToCreate(String description) {
        if (description != null && validatorHelper.validateStringParameter(CERTIFICATE_DESCRIPTION_REGEX, description)) {
            return;
        }
        throw new CertificateValidatorException("certificate.description.not.valid", description);
    }

    private void validateCertificateDescriptionToUpdate(String description) {
        if (description != null && !validatorHelper.validateStringParameter(CERTIFICATE_DESCRIPTION_REGEX, description)) {
            throw new CertificateValidatorException("certificate.description.not.valid", description);
        }
    }

    private void validateCertificatePriceToCreate(Double price) {
        if (price != null && price > 0) {
            return;
        }
        throw new CertificateValidatorException("certificate.price.not.valid", price);
    }

    private void validateCertificatePriceToUpdate(Double price) {
        if (price != null && price <= 0) {
            throw new CertificateValidatorException("certificate.price.not.valid", price);
        }
    }

    private void validateCertificateDurationToCreate(Integer duration) {
        if (duration != null && duration > 0 && duration <= 365) {
            return;
        }
        throw new CertificateValidatorException("certificate.duration.not.valid", duration);
    }

    private void validateCertificateDurationToUpdate(Integer duration) {
        if (duration != null && (duration <= 0 || duration > 365)) {
            throw new CertificateValidatorException("certificate.duration.not.valid", duration);
        }
    }
}
