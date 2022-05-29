package com.epam.esm.dao.audit;

import com.epam.esm.domain.User;

import javax.persistence.PrePersist;

public class AuditUserListener {

    @PrePersist
    public void createUser(User user) {
        user.setState(0);
    }
}
