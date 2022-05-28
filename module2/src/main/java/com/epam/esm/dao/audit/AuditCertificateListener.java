package com.epam.esm.dao.audit;

import com.epam.esm.domain.Certificate;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class AuditCertificateListener {
    @PrePersist
    public void createCertificate(Certificate certificate) {
        setCreateDate(certificate);
        setUpdateDate(certificate);
        certificate.setState(0);
    }

    @PreUpdate
    public void updateCertificate(Certificate certificate) {
        setUpdateDate(certificate);
    }

    private void setCreateDate(Certificate certificate) {
        LocalDateTime creationDate = LocalDateTime.now();
        certificate.setCreationDate(creationDate);
    }

    private void setUpdateDate(Certificate certificate) {
        LocalDateTime updateDate = LocalDateTime.now();
        certificate.setLastUpdateDate(updateDate);
    }
}
