package com.epam.esm.dao.audit;

import com.epam.esm.domain.Tag;

import javax.persistence.PrePersist;

public class AuditTagListener {

    @PrePersist
    public void createTag(Tag tag) {
        tag.setState(0);
    }
}
