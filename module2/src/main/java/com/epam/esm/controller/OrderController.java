package com.epam.esm.controller;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Order;
import com.epam.esm.domain.model.OrderDataModel;
import com.epam.esm.domain.model.OrderModel;
import com.epam.esm.service.OrderService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Validated
@RestController
@RequestMapping(value = "/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @JsonView(OrderModel.Views.V1.class)
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderModel> getOrderById(@PathVariable @NotNull Long id) {
        Order order = orderService.getOrderById(id);
        OrderModel orderView = OrderModel.createForm(order);

        return new ResponseEntity<>(orderView, HttpStatus.OK);
    }

    @JsonView(OrderModel.Views.V1.class)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderModel>> getOrders(
            @RequestParam(name = "pageNumber", required = false) @Positive Integer pageNumber,
            @RequestParam(name = "pageSize", required = false) @Positive Integer pageSize) {
        List<Order> orders = orderService.getOrders(pageNumber, pageSize);
        List<OrderModel> orderModels = OrderModel.createListForm(orders);

        return new ResponseEntity<>(orderModels, HttpStatus.OK);
    }

    @JsonView(OrderModel.Views.V1.class)
    @GetMapping(path = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderModel>> getOrdersByUserId(@PathVariable("id") @Positive Long id,
                                                              @RequestParam(name = "pageNumber", required = false) @Positive Integer pageNumber,
                                                              @RequestParam(name = "pageSize", required = false) @Positive Integer pageSize) {
        List<Order> orders = orderService.getOrdersByUserId(id, pageNumber, pageSize);
        List<OrderModel> orderModels = OrderModel.createListForm(orders);

        return new ResponseEntity<>(orderModels, HttpStatus.OK);
    }

    @JsonView(OrderDataModel.Views.V1.class)
    @GetMapping(path = "/{orderId}/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDataModel> getDataByUserId(@PathVariable("userId") @NotNull @Positive Long userId,
                                                         @PathVariable("orderId") @NotNull @Positive Long orderId) {
        Order order = orderService.getOrderDataByUserId(userId, orderId);
        OrderDataModel orderDataView = OrderDataModel.createForm(order);

        return new ResponseEntity<>(orderDataView, HttpStatus.OK);
    }

    @JsonView(OrderModel.Views.V1.class)
    @PostMapping(path = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderModel> makeOrder(
            @PathVariable("id") @NotNull @Positive Long id,
            @RequestParam("certificateIds") @NotNull @NotEmpty List<Long> certificateIds) {
        Order order = orderService.makeOrder(id, certificateIds);
        OrderModel orderModel = OrderModel.createForm(order);

        return new ResponseEntity<>(orderModel, HttpStatus.OK);
    }
}
