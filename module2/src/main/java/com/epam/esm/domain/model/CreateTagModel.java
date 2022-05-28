package com.epam.esm.domain.model;

import com.epam.esm.domain.Tag;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTagModel {
    @Null
    @JsonView(Views.V1.class)
    private Long id;

    @NotBlank
    @Pattern(regexp = "^\\S{1,70}$")
    @JsonView(Views.V1.class)
    private String name;

    public class Views {
        public class V1 {
        }
    }

    public static Tag createForm(CreateTagModel createTagModel) {
        Tag tag = new Tag();
        tag.setName(createTagModel.getName());
        return tag;
    }

    public static Tag createForm(TagModel tagModel) {
        Tag tag = new Tag();
        tag.setName(tagModel.getName());
        return tag;
    }

    public static List<Tag> createListForm(List<TagModel> tagModels) {
        return tagModels.stream().map(CreateTagModel::createForm).collect(Collectors.toList());
    }
}
