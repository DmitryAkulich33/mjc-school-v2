package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Certificate_;
import com.epam.esm.exception.CertificateDaoException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Repository
public class CertificateDaoImpl implements CertificateDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Certificate> getCertificateById(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Certificate> criteriaQuery = criteriaBuilder.createQuery(Certificate.class);
        Root<Certificate> root = criteriaQuery.from(Certificate.class);
        criteriaQuery.select(root).distinct(true).where(criteriaBuilder.equal(root.get(Certificate_.id), id),
                criteriaBuilder.equal(root.get(Certificate_.state), 0));

        try {
            return Optional.of(entityManager.createQuery(criteriaQuery).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
        catch (IllegalArgumentException | PersistenceException e) {
            throw new CertificateDaoException("server.error");
        }
    }
}
