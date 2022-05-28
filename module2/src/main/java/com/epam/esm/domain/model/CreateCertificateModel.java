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
        Certificate certificate = new Certificate();
        certificate.setId(createCertificateModel.getId());
        certificate.setName(createCertificateModel.getName());
        certificate.setDescription(createCertificateModel.getDescription());
        certificate.setPrice(createCertificateModel.getPrice());
        certificate.setCreationDate(createCertificateModel.getCreateDate());
        certificate.setLastUpdateDate(createCertificateModel.getLastUpdateDate());
        certificate.setDuration(createCertificateModel.getDuration());
        certificate.setTags(CreateTagModel.createListForm(createCertificateModel.getTags()));

        return certificate;
    }
}
