package com.epam.esm.domain;

import com.epam.esm.dao.audit.AuditUserListener;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@Entity(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "orders")
@EqualsAndHashCode(exclude = "orders")
@EntityListeners(AuditUserListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(name = "state")
    private Integer state;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orders;
}
