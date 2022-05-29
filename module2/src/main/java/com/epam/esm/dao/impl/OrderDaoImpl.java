package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.domain.Order;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderDaoImpl implements OrderDao {
    @Override
    public Optional<Order> getOrderById(Long idOrder) {
        return Optional.empty();
    }

    @Override
    public List<Order> getOrders(Integer offset, Integer pageSize) {
        return null;
    }

    @Override
    public List<Order> getOrdersByUserId(Long idUser, Integer offset, Integer pageSize) {
        return null;
    }

    @Override
    public Optional<Order> getOrderDataByUserId(Long idUser, Long idOrder) {
        return Optional.empty();
    }

    @Override
    public Order createOrder(Order order) {
        return null;
    }
}
