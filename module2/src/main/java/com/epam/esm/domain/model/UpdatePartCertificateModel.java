package com.epam.esm.domain.model;

import com.epam.esm.domain.Certificate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePartCertificateModel {
    @Null
    @JsonView(Views.V1.class)
    private Long id;

    @Pattern(regexp = "^([A-Z].{0,99})$")
    @JsonView(Views.V1.class)
    private String name;

    @Pattern(regexp = "^([A-Z].{0,299})$")
    @JsonView(Views.V1.class)
    private String description;

    @Positive
    @JsonView(Views.V1.class)
    private Double price;

    @JsonView(Views.V1.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    @JsonView(Views.V1.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdateDate;

    @Positive
    @JsonView(Views.V1.class)
    private Integer duration;

    @JsonView(Views.V1.class)
    private List<TagModel> tags;

    public class Views {
        public class V1 {
        }
    }

    public static Certificate createForm(UpdatePartCertificateModel updatePartCertificateModel) {
        List<TagModel> tagModels = updatePartCertificateModel.getTags();
        return Certificate.builder()
                .id(updatePartCertificateModel.getId())
                .name(updatePartCertificateModel.getName())
                .description(updatePartCertificateModel.getDescription())
                .price(updatePartCertificateModel.getPrice())
                .creationDate(updatePartCertificateModel.getCreateDate())
                .lastUpdateDate(updatePartCertificateModel.getLastUpdateDate())
                .duration(updatePartCertificateModel.getDuration())
                .tags(tagModels != null ? CreateTagModel.createListForm(tagModels) : null)
                .build();
    }
}
