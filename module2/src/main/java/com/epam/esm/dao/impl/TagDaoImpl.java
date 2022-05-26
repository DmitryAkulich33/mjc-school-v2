package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Tag;
import com.epam.esm.exception.TagDaoException;
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
public class TagDaoImpl implements TagDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Tag> getTagById(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        criteriaQuery.select(root).distinct(true).where(criteriaBuilder.equal(root.get("id"), id),
                criteriaBuilder.equal(root.get("state"), 0));

        try {
            return Optional.of(entityManager.createQuery(criteriaQuery).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (IllegalArgumentException | PersistenceException e) {
            throw new TagDaoException("server.error");
        }
    }
}
