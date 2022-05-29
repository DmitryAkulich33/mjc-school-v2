package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.domain.User;
import com.epam.esm.domain.User_;
import com.epam.esm.exception.PaginationException;
import com.epam.esm.exception.UserDaoException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<User> getUserById(Long idUser) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(User_.id), idUser));
        try {
            return Optional.of(entityManager.createQuery(criteriaQuery).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (IllegalArgumentException | PersistenceException e) {
            throw new UserDaoException("server.error");
        }
    }

    @Override
    public List<User> getUsers(Integer offset, Integer pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        checkPagination(offset, criteriaBuilder);

        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(User_.state), 0));

        try {
            return entityManager.createQuery(criteriaQuery)
                    .setFirstResult(offset)
                    .setMaxResults(pageSize)
                    .getResultList();
        } catch (IllegalArgumentException e) {
            throw new UserDaoException("server.error");
        }
    }

    @Override
    public List<User> createUsers(List<User> users) {
        return null;
    }

    @Override
    public User getUserWithTheLargeSumOrders() {
        return null;
    }

    private void checkPagination(Integer offset, CriteriaBuilder criteriaBuilder) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Long count = entityManager.createQuery(countQuery.select(criteriaBuilder.count(countQuery.from(User.class)))).getSingleResult();
        if (count <= offset) {
            throw new PaginationException("pagination.more.than.users", count);
        }
    }
}
