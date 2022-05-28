package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Certificate_;
import com.epam.esm.exception.CertificateDaoException;
import com.epam.esm.exception.CertificateDuplicateException;
import com.epam.esm.exception.CertificateNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
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

    @Override
    public void deleteCertificate(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<Certificate> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Certificate.class);
        Root<Certificate> root = criteriaUpdate.from(Certificate.class);
        criteriaUpdate.set(Certificate_.state, 1);
        criteriaUpdate.where(criteriaBuilder.equal(root.get(Certificate_.id), id));
        entityManager.createQuery(criteriaUpdate).executeUpdate();
        try {
            entityManager.createQuery(criteriaUpdate).executeUpdate();
        } catch (IllegalArgumentException | PersistenceException e) {
            throw new CertificateDaoException("server.error");
        }
    }

    @Override
    public Certificate createCertificate(Certificate certificate) {
        try {
            entityManager.persist(certificate);
        } catch (IllegalArgumentException e) {
            throw new CertificateDaoException("server.error");
        } catch (PersistenceException e) {
            throw new CertificateDuplicateException("certificate.exists", certificate.getName());
        }
        return certificate;
    }
}
