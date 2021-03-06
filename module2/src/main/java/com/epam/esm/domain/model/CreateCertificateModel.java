package com.epam.esm.domain.model;

import com.epam.esm.domain.Certificate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCertificateModel {
    @JsonView(Views.V1.class)
    private Long id;

    @NotBlank()
    @Pattern(regexp = "^([A-Z].{0,99})$")
    @JsonView(Views.V1.class)
    private String name;

    @NotBlank()
    @Pattern(regexp = "^([A-Z].{0,299})$")
    @JsonView(Views.V1.class)
    private String description;

    @NotNull
    @Positive
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

    public static Certificate createForm(CreateCertificateModel createCertificateModel) {
        return Certificate.builder()
                .id(createCertificateModel.getId())
                .name(createCertificateModel.getName())
                .description(createCertificateModel.getDescription())
                .price(createCertificateModel.getPrice())
                .creationDate(createCertificateModel.getCreateDate())
                .lastUpdateDate(createCertificateModel.getLastUpdateDate())
                .duration(createCertificateModel.getDuration())
                .tags(CreateTagModel.createListForm(createCertificateModel.getTags()))
                .build();
    }
}
