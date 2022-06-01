package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.domain.Order;
import com.epam.esm.domain.Order_;
import com.epam.esm.domain.User;
import com.epam.esm.domain.User_;
import com.epam.esm.exception.OrderDaoException;
import com.epam.esm.exception.OrderNotFoundException;
import com.epam.esm.exception.PaginationException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderDaoImpl implements OrderDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Order> getOrderById(Long idOrder) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(Order_.id), idOrder));
        try {
            return Optional.of(entityManager.createQuery(criteriaQuery).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (IllegalArgumentException | PersistenceException e) {
            throw new OrderDaoException("server.error");
        }
    }

    @Override
    public List<Order> getOrders(Integer offset, Integer pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        checkPagination(offset, criteriaBuilder);
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(Order_.state), 0));

        try {
            return entityManager.createQuery(criteriaQuery)
                    .setFirstResult(offset)
                    .setMaxResults(pageSize)
                    .getResultList();
        } catch (IllegalArgumentException e) {
            throw new OrderDaoException("server.error");
        }
    }

    @Override
    public List<Order> getOrdersByUserId(Long id, Integer offset, Integer pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        checkPagination(offset, criteriaBuilder);
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> rootOrder = criteriaQuery.from(Order.class);

        Subquery<User> userSubquery = criteriaQuery.subquery(User.class);
        Root<User> userRoot = userSubquery.from(User.class);
        userSubquery.select(userRoot).where(criteriaBuilder.equal(userRoot.get(User_.id), id));
        criteriaQuery.select(rootOrder).where(criteriaBuilder.equal(rootOrder.get(Order_.state), 0),
                criteriaBuilder.in(rootOrder.get(Order_.user)).value(userSubquery));

        try {
            return entityManager.createQuery(criteriaQuery)
                    .setFirstResult(offset)
                    .setMaxResults(pageSize)
                    .getResultList();
        } catch (IllegalArgumentException e) {
            throw new OrderDaoException("server.error");
        } catch (PersistenceException e) {
            throw new OrderNotFoundException("order.id.not.found", e);
        }
    }

    @Override
    public Optional<Order> getOrderDataByUserId(Long userId, Long orderId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> rootOrder = criteriaQuery.from(Order.class);

        Subquery<User> userSubquery = criteriaQuery.subquery(User.class);
        Root<User> userRoot = userSubquery.from(User.class);
        userSubquery.select(userRoot).where(criteriaBuilder.equal(userRoot.get(User_.id), userId));

        criteriaQuery.select(rootOrder).where(criteriaBuilder.equal(rootOrder.get(Order_.id), orderId),
                criteriaBuilder.in(rootOrder.get(Order_.user)).value(userSubquery));

        try {
            return Optional.of(entityManager.createQuery(criteriaQuery).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (IllegalArgumentException | PersistenceException e) {
            throw new OrderDaoException("server.error");
        }
    }

    @Override
    public Order createOrder(Order order) {
        try {
            entityManager.persist(order);
        } catch (IllegalArgumentException | PersistenceException e) {
            throw new OrderDaoException("server.error");
        }
        return order;
    }

    private void checkPagination(Integer offset, CriteriaBuilder criteriaBuilder) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Long count = entityManager.createQuery(countQuery.select(criteriaBuilder.count(countQuery.from(Order.class)))).getSingleResult();
        if (count <= offset) {
            throw new PaginationException("pagination.more.than.orders", count);
        }
    }
}
