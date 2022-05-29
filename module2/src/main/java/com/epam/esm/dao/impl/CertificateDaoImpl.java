package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Certificate_;
import com.epam.esm.domain.Tag;
import com.epam.esm.domain.Tag_;
import com.epam.esm.exception.CertificateDaoException;
import com.epam.esm.exception.CertificateDuplicateException;
import com.epam.esm.exception.PaginationException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
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
        } catch (IllegalArgumentException | PersistenceException e) {
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

    @Override
    public Certificate updateCertificate(Certificate certificate) {
        try {
            return entityManager.merge(certificate);
        } catch (IllegalArgumentException | PersistenceException e) {
            throw new CertificateDaoException("server.error");
        }
    }

    @Override
    public List<Certificate> getCertificates(String tagName, String searchQuery, Boolean sortAsc, String sortField, Integer offset, Integer pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        checkPagination(offset, criteriaBuilder);
        CriteriaQuery<Certificate> criteriaQuery = criteriaBuilder.createQuery(Certificate.class);
        Root<Certificate> root = criteriaQuery.from(Certificate.class);
        List<Predicate> conditions = getPredicatesForFilter(tagName, searchQuery, root, criteriaBuilder);

        try {
            return entityManager.createQuery(getCertificateCriteriaQuery(conditions, criteriaQuery, root, criteriaBuilder, sortField, sortAsc))
                    .setFirstResult(offset)
                    .setMaxResults(pageSize)
                    .getResultList();
        } catch (IllegalArgumentException | PersistenceException e) {
            throw new CertificateDaoException("server.error");
        }
    }

    @Override
    public List<Certificate> getCertificates(String tagName, String searchQuery, Boolean sortAsc, String sortField) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Certificate> criteriaQuery = criteriaBuilder.createQuery(Certificate.class);
        Root<Certificate> root = criteriaQuery.from(Certificate.class);
        List<Predicate> conditions = getPredicatesForFilter(tagName, searchQuery, root, criteriaBuilder);

        try {
            return entityManager.createQuery(getCertificateCriteriaQuery(conditions, criteriaQuery, root, criteriaBuilder, sortField, sortAsc))
                    .getResultList();
        } catch (IllegalArgumentException | PersistenceException e) {
            throw new CertificateDaoException("server.error");
        }
    }

    @Override
    public List<Certificate> getCertificatesByTags(List<String> tagNames, Integer offset, Integer pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Certificate> criteriaQuery = criteriaBuilder.createQuery(Certificate.class);
        Root<Certificate> root = criteriaQuery.from(Certificate.class);

        List<Predicate> conditions = getPredicatesForSearchByTags(tagNames, root, criteriaBuilder);

        criteriaQuery.select(root).distinct(true).where(criteriaBuilder.and(conditions.toArray(new Predicate[0])),
                criteriaBuilder.equal(root.get(Certificate_.state), 0));

        try {
            return entityManager.createQuery(criteriaQuery)
                    .setFirstResult(offset)
                    .setMaxResults(pageSize)
                    .getResultList();
        } catch (IllegalArgumentException | PersistenceException e) {
            throw new CertificateDaoException("server.error");
        }
    }

    @Override
    public List<Certificate> getCertificatesByTags(List<String> tagNames) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Certificate> criteriaQuery = criteriaBuilder.createQuery(Certificate.class);
        Root<Certificate> root = criteriaQuery.from(Certificate.class);

        List<Predicate> conditions = getPredicatesForSearchByTags(tagNames, root, criteriaBuilder);

        criteriaQuery.select(root).distinct(true).where(criteriaBuilder.and(conditions.toArray(new Predicate[0])),
                criteriaBuilder.equal(root.get(Certificate_.state), 0));

        try {
            return entityManager.createQuery(criteriaQuery)
                    .getResultList();
        } catch (IllegalArgumentException | PersistenceException e) {
            throw new CertificateDaoException("message.wrong_data", e);
        }
    }

    private List<Predicate> getPredicatesForSearchByTags(List<String> tagNames, Root<Certificate> root, CriteriaBuilder criteriaBuilder) {
        List<Predicate> conditions = new ArrayList<>();
        if (tagNames != null && !tagNames.isEmpty()) {
            for (String tagName : tagNames) {
                Join<Certificate, Tag> join = root.join(Certificate_.tags, JoinType.INNER);
                conditions.add(criteriaBuilder.equal(join.get(Tag_.name), tagName));
            }
        }
        return conditions;
    }


    private void checkPagination(Integer offset, CriteriaBuilder criteriaBuilder) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Long count = entityManager.createQuery(countQuery.select(criteriaBuilder.count(countQuery.from(Certificate.class)))).getSingleResult();
        if (count <= offset) {
            throw new PaginationException("pagination.more.than.certificates", count);
        }
    }

    private List<Predicate> getPredicatesForFilter(String tagName, String searchQuery, Root<Certificate> root, CriteriaBuilder criteriaBuilder) {
        List<Predicate> conditions = new ArrayList<>();
        if (tagName != null) {
            Join<Certificate, Tag> join = root.join(Certificate_.tags, JoinType.INNER);
            Predicate tagPredicate = criteriaBuilder.equal(join.get(Tag_.name), tagName);
            conditions.add(tagPredicate);
        }

        if (searchQuery != null) {
            Predicate searchCondition = criteriaBuilder.or(criteriaBuilder.like(root.get(Certificate_.description), "%" + searchQuery + "%"),
                    criteriaBuilder.like(root.get(Certificate_.name), "%" + searchQuery + "%"));
            conditions.add(searchCondition);
        }

        Predicate notLock = criteriaBuilder.equal(root.get(Certificate_.state), 0);
        conditions.add(notLock);

        return conditions;
    }

    private CriteriaQuery<Certificate> getCertificateCriteriaQuery(List<Predicate> conditions, CriteriaQuery<Certificate> criteriaQuery,
                                                                   Root<Certificate> root, CriteriaBuilder criteriaBuilder,
                                                                   String sortField, Boolean sortAsc) {
        return (sortAsc == null) ? selectWithoutSort(conditions, criteriaQuery, root, criteriaBuilder) :
                selectSort(conditions, criteriaQuery, root, criteriaBuilder, sortField, sortAsc);
    }

    private CriteriaQuery<Certificate> selectSort(List<Predicate> conditions, CriteriaQuery<Certificate> criteriaQuery,
                                                  Root<Certificate> root, CriteriaBuilder criteriaBuilder,
                                                  String sortField, Boolean sortAsc) {
        return (sortAsc) ? selectSortAsc(conditions, criteriaQuery, root, criteriaBuilder, sortField) :
                selectSortDesc(conditions, criteriaQuery, root, criteriaBuilder, sortField);
    }

    private CriteriaQuery<Certificate> selectSortAsc(List<Predicate> conditions, CriteriaQuery<Certificate> criteriaQuery,
                                                     Root<Certificate> root, CriteriaBuilder criteriaBuilder, String sortField) {
        return criteriaQuery.select(root)
                .distinct(true)
                .where(criteriaBuilder.and(conditions.toArray(new Predicate[0])))
                .orderBy(criteriaBuilder.asc(root.get(sortField)));
    }

    private CriteriaQuery<Certificate> selectSortDesc(List<Predicate> conditions, CriteriaQuery<Certificate> criteriaQuery,
                                                      Root<Certificate> root, CriteriaBuilder criteriaBuilder, String sortField) {
        return criteriaQuery.select(root)
                .distinct(true)
                .where(criteriaBuilder.and(conditions.toArray(new Predicate[0])))
                .orderBy(criteriaBuilder.desc(root.get(sortField)));
    }

    private CriteriaQuery<Certificate> selectWithoutSort(List<Predicate> conditions, CriteriaQuery<Certificate> criteriaQuery,
                                                         Root<Certificate> root, CriteriaBuilder criteriaBuilder) {
        return criteriaQuery.select(root)
                .distinct(true)
                .where(criteriaBuilder.and(conditions.toArray(new Predicate[0])));
    }

}
