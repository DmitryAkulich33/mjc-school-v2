package com.epam.esm.domain.model;

import com.epam.esm.domain.Certificate;
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
public class CertificateModel {
    @JsonView({Views.V1.class, OrderModel.Views.V1.class})
    private Long id;

    @JsonView({Views.V1.class, OrderModel.Views.V1.class})
    private String name;

    @JsonView({Views.V1.class, OrderModel.Views.V1.class})
    private String description;

    @JsonView({Views.V1.class, OrderModel.Views.V1.class})
    private Double price;

    @JsonView({Views.V1.class, OrderModel.Views.V1.class})
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    @JsonView({Views.V1.class, OrderModel.Views.V1.class})
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdateDate;

    @JsonView({Views.V1.class, OrderModel.Views.V1.class})
    private Integer duration;

    @JsonView({Views.V1.class, OrderModel.Views.V1.class})
    private List<TagModel> tags;

    public class Views {
        public class V1 {
        }
    }

    public static CertificateModel createForm(Certificate certificate) {
        return CertificateModel.builder()
                .id(certificate.getId())
                .name(certificate.getName())
                .description(certificate.getDescription())
                .price(certificate.getPrice())
                .createDate(certificate.getCreationDate())
                .lastUpdateDate(certificate.getLastUpdateDate())
                .duration(certificate.getDuration())
                .tags(TagModel.createListForm(certificate.getTags()))
                .build();
    }

    public static List<CertificateModel> createListForm(List<Certificate> certificates) {
        return certificates.stream().map(CertificateModel::createForm).collect(Collectors.toList());
    }
}
