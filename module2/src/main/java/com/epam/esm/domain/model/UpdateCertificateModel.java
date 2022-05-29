package com.epam.esm.domain.model;

import com.epam.esm.domain.Certificate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCertificateModel {
    @Null
    @JsonView(Views.V1.class)
    private Long id;

    @NotBlank
    @Pattern(regexp = "^([A-Z].{0,99})$")
    @JsonView(Views.V1.class)
    private String name;

    @NotBlank
    @Pattern(regexp = "^([A-Z].{0,299})$")
    @JsonView(Views.V1.class)
    private String description;

    @NotNull
    @JsonView(Views.V1.class)
    private Double price;

    @JsonView(Views.V1.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    @JsonView(Views.V1.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdateDate;

    @NotNull
    @Positive
    @JsonView(Views.V1.class)
    private Integer duration;

    @NotNull
    @NotEmpty
    @JsonView(Views.V1.class)
    private List<TagModel> tags;

    public class Views {
        public class V1 {
        }
    }

    public static Certificate createForm(UpdateCertificateModel updateCertificateModel) {
        List<TagModel> tagModels = updateCertificateModel.getTags();
        return Certificate.builder()
                .id(updateCertificateModel.getId())
                .name(updateCertificateModel.getName())
                .description(updateCertificateModel.getDescription())
                .price(updateCertificateModel.getPrice())
                .creationDate(updateCertificateModel.getCreateDate())
                .lastUpdateDate(updateCertificateModel.getLastUpdateDate())
                .duration(updateCertificateModel.getDuration())
                .tags(tagModels != null ? CreateTagModel.createListForm(tagModels) : null)
                .build();
    }
}
