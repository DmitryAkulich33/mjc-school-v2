package com.epam.esm.service.impl;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Order;
import com.epam.esm.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Override
    public Order getOrderById(Long idOrder) {
        return null;
    }

    @Override
    public List<Order> getOrders(Integer pageNumber, Integer pageSize) {
        return null;
    }

    @Override
    public List<Order> getOrdersByUserId(Long idUser, Integer pageNumber, Integer pageSize) {
        return null;
    }

    @Override
    public Order getOrderDataByUserId(Long idUser, Long idOrder) {
        return null;
    }

    @Override
    public Order makeOrder(Long idUser, List<Certificate> certificates) {
        return null;
    }
}
