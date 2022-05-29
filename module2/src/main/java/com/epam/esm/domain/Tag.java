package com.epam.esm.domain;

import com.epam.esm.dao.audit.AuditTagListener;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@Builder
@Entity(name = "tag")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "certificates")
@EqualsAndHashCode(exclude = "certificates")
@EntityListeners(AuditTagListener.class)
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tag", updatable = false, nullable = false)
    private Long id;

    @Pattern(regexp = "^\\S{1,70}$")
    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "state")
    private Integer state;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    List<Certificate> certificates;
}
