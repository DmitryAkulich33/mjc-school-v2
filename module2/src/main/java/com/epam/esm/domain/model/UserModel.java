package com.epam.esm.domain.model;

import com.epam.esm.domain.User;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserModel {
    @JsonView({Views.V1.class})
    private Long id;

    @JsonView({Views.V1.class})
    private String name;

    @JsonView({Views.V1.class})
    private String surname;

    public class Views {
        public class V1 {
        }
    }

    public static UserModel createForm(User user) {
        return UserModel.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .build();
    }

    public static List<UserModel> createListForm(List<User> users) {
        return users.stream().map(UserModel::createForm).collect(Collectors.toList());
    }
}
