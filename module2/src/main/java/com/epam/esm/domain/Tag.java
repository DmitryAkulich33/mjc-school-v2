package com.epam.esm.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@Entity(name = "tag")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "certificates")
@EqualsAndHashCode(exclude = "certificates")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "state")
    private Integer state;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    List<Certificate> certificates;
}
