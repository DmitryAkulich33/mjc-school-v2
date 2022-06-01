package com.epam.esm.domain.model;

import com.epam.esm.domain.Order;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderModel {
    @JsonView(Views.V1.class)
    private Long id;

    @JsonView(Views.V1.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime purchaseDate;

    @JsonView(Views.V1.class)
    private Double total;

    @JsonView(Views.V1.class)
    private UserModel user;

    @JsonView(Views.V1.class)
    private List<CertificateModel> certificates;

    public class Views {
        public class V1 {
        }
    }

    public static OrderModel createForm(Order order) {
        return OrderModel.builder()
                .id(order.getId())
                .purchaseDate(order.getPurchaseDate())
                .total(order.getTotal())
                .user(UserModel.createForm(order.getUser()))
                .certificates(CertificateModel.createListForm(order.getCertificates()))
                .build();
    }

    public static List<OrderModel> createListForm(List<Order> orders) {
        return orders.stream().map(OrderModel::createForm).collect(Collectors.toList());
    }
}
