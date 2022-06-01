package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Order;
import com.epam.esm.exception.OrderNotFoundException;
import com.epam.esm.exception.PaginationException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.util.OffsetCalculator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;
    private final UserService userService;
    private final CertificateService certificateService;

    private static Logger log = LogManager.getLogger(OrderServiceImpl.class);

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, UserService userService, CertificateService certificateService) {
        this.orderDao = orderDao;
        this.userService = userService;
        this.certificateService = certificateService;
    }

    @Override
    public Order getOrderById(Long id) {
        log.debug(String.format("Search order by id %d", id));
        Optional<Order> optionalOrder = orderDao.getOrderById(id);
        return optionalOrder.orElseThrow(() -> new OrderNotFoundException("order.id.not.found", id));
    }

    @Override
    public List<Order> getOrders(Integer pageNumber, Integer pageSize) {
        log.debug("Getting all orders.");
        if (pageNumber != null && pageSize != null) {
            Integer offset = OffsetCalculator.calculateOffset(pageNumber, pageSize);
            return orderDao.getOrders(offset, pageSize);
        } else {
            throw new PaginationException("pagination.not.valid.data", pageNumber, pageSize);
        }
    }

    @Override
    public List<Order> getOrdersByUserId(Long id, Integer pageNumber, Integer pageSize) {
        log.debug(String.format("Getting order by user id: %d", id));
        userService.getUserById(id);
        if (pageNumber != null && pageSize != null) {
            Integer offset = OffsetCalculator.calculateOffset(pageNumber, pageSize);
            return orderDao.getOrdersByUserId(id, offset, pageSize);
        } else {
            throw new PaginationException("pagination.not.valid.data", pageNumber, pageSize);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Order getOrderDataByUserId(Long userId, Long orderId) {
        log.debug(String.format("Search order by id_order %d and id_user %d", orderId, userId));
        userService.getUserById(userId);
        Optional<Order> optionalOrder = orderDao.getOrderDataByUserId(userId, orderId);
        return optionalOrder.orElseThrow(() -> new OrderNotFoundException("order.id.not.found", orderId));
    }

    @Transactional
    @Override
    public Order makeOrder(Long userId, List<Long> certificateIds) {
        log.debug(String.format("Make order, userId: %d, certificates: %s", userId, certificateIds));
        List<Certificate> certificates = getCertificates(certificateIds);

        Order order = Order.builder()
                .certificates(certificates)
                .total(getTotal(certificates))
                .user(userService.getUserById(userId))
                .build();

        return orderDao.createOrder(order);
    }

    private List<Certificate> getCertificates(List<Long> certificateIds) {
        return certificateIds.stream()
                .map(certificateService::getCertificateById)
                .collect(Collectors.toList());
    }

    double getTotal(List<Certificate> certificates) {
        return certificates.stream().mapToDouble(Certificate::getPrice).sum();
    }
}
