package com.epam.esm.domain.model;

import com.epam.esm.domain.Order;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDataModel {
    @JsonView(Views.V1.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime purchaseDate;

    @JsonView(Views.V1.class)
    private Double total;

    public class Views {
        public class V1 {
        }
    }

    public static OrderDataModel createForm(Order order) {
        return OrderDataModel.builder()
                .purchaseDate(order.getPurchaseDate())
                .total(order.getTotal())
                .build();
    }
}
