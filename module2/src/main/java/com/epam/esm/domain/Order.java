package com.epam.esm.domain;

import com.epam.esm.dao.audit.AuditOrderListener;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Entity(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditOrderListener.class)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order", updatable = false, nullable = false, insertable = false)
    private Long id;

    @Column(name = "purchase_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime purchaseDate;

    @Column
    private Double total;

    @Column(name = "state")
    private Integer state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "certificate_order",
            joinColumns = {@JoinColumn(name = "order_id", referencedColumnName = "id_order")},
            inverseJoinColumns = {@JoinColumn(name = "certificate_id", referencedColumnName = "id_certificate")})
    private List<Certificate> certificates;
}
