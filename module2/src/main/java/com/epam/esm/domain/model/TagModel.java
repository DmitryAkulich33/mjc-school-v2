package com.epam.esm.domain.model;

import com.epam.esm.domain.Tag;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagModel {
    @JsonView({Views.V1.class, CertificateModel.Views.V1.class, OrderModel.Views.V1.class})
    private Long id;

    @NotBlank
    @Pattern(regexp = "^\\S{1,70}$")
    @JsonView({Views.V1.class,
            CertificateModel.Views.V1.class,
            CreateCertificateModel.Views.V1.class,
            CreateTagModel.Views.V1.class,
            UpdateCertificateModel.Views.V1.class,
            UpdatePartCertificateModel.Views.V1.class,
            OrderModel.Views.V1.class})
    private String name;

    public class Views {
        public class V1 {
        }
    }

    public static TagModel createForm(Tag tag) {
        return TagModel.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
    }

    public static List<TagModel> createListForm(List<Tag> tags) {
        return tags.stream().map(TagModel::createForm).collect(Collectors.toList());
    }
}
