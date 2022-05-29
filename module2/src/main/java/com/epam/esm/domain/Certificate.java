package com.epam.esm.domain;

import com.epam.esm.dao.audit.AuditCertificateListener;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Entity(name = "certificate")
@ToString(exclude = "orders")
@EqualsAndHashCode(exclude = "orders")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditCertificateListener.class)
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_certificate", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_date")
    private LocalDateTime lastUpdateDate;

    @Column(name = "state")
    private Integer state;

    @Column(name = "duration")
    private Integer duration;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tag_certificate",
            joinColumns = @JoinColumn(name = "certificate_id", referencedColumnName = "id_certificate"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id_tag"))
    private List<Tag> tags;

    @ManyToMany(mappedBy = "certificates", fetch = FetchType.LAZY)
    private List<Order> orders;
}
