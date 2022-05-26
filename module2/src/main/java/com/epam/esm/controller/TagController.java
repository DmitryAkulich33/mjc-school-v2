package com.epam.esm.controller;

import com.epam.esm.domain.Tag;
import com.epam.esm.domain.model.TagModel;
import com.epam.esm.service.TagService;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Validated
@RestController
@RequestMapping(value = "/api/v1/tags")
public class TagController {
    private final TagService tagService;
    private static Logger log = LogManager.getLogger(TagController.class);

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @JsonView(TagModel.Views.V1.class)
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagModel> getTagById(@PathVariable("id") @NotNull @Positive Long id) {
        log.debug("Getting tag with id: " + id);
        Tag tag = tagService.getTagById(id);
        TagModel tagModel = TagModel.createForm(tag);

        return new ResponseEntity<>(tagModel, HttpStatus.OK);
    }
}
