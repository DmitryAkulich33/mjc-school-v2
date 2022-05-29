package com.epam.esm.controller;

import com.epam.esm.domain.User;
import com.epam.esm.domain.model.UserModel;
import com.epam.esm.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Validated
@RestController
@RequestMapping(value = "/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @JsonView(UserModel.Views.V1.class)
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserModel> getUserById(@PathVariable("id") @NotNull @Positive Long id) {
        User user = userService.getUserById(id);
        UserModel userModel = UserModel.createForm(user);

        return new ResponseEntity<>(userModel, HttpStatus.OK);
    }

    @JsonView(UserModel.Views.V1.class)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserModel>> getUsers(
            @RequestParam(name = "pageNumber", required = false) @Positive Integer pageNumber,
            @RequestParam(name = "pageSize", required = false) @Positive Integer pageSize) {
        List<User> users = userService.getUsers(pageNumber, pageSize);
        List<UserModel> userModels = UserModel.createListForm(users);

        return new ResponseEntity<>(userModels, HttpStatus.OK);
    }
}
