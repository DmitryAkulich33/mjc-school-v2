package com.epam.esm.service;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Order;

import java.util.List;

public interface OrderService {
    Order getOrderById(Long id);

    List<Order> getOrders(Integer pageNumber, Integer pageSize);

    List<Order> getOrdersByUserId(Long id, Integer pageNumber, Integer pageSize);

    Order getOrderDataByUserId(Long userId, Long orderId);

    Order makeOrder(Long id, List<Long> certificateIds);
}
