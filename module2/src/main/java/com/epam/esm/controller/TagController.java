package com.epam.esm.controller;

import com.epam.esm.domain.Tag;
import com.epam.esm.domain.model.CreateTagModel;
import com.epam.esm.domain.model.TagModel;
import com.epam.esm.service.TagService;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Validated
@RestController
@RequestMapping(value = "/api/v1/tags")
public class TagController {
    private final TagService tagService;
    private static Logger log = LogManager.getLogger(TagController.class);

    @Autowired
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

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Tag> deleteTag(@PathVariable("id") @NotNull @Positive Long id) {
        tagService.deleteTag(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @JsonView(TagModel.Views.V1.class)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagModel> createTag(@Valid @RequestBody @JsonView(CreateTagModel.Views.V1.class) CreateTagModel createTagModel) {
        Tag tag = CreateTagModel.createForm(createTagModel);
        Tag createdTag = tagService.createTag(tag);
        TagModel view = TagModel.createForm(createdTag);

        return new ResponseEntity<>(view, HttpStatus.CREATED);
    }

    @JsonView(TagModel.Views.V1.class)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TagModel>> getAllTags(@RequestParam(name = "pageNumber", required = false) @Positive Integer pageNumber,
                                                     @RequestParam(name = "pageSize", required = false) @Positive Integer pageSize) {
        List<Tag> tags = tagService.getTags(pageNumber, pageSize);
        List<TagModel> tagModels = TagModel.createListForm(tags);

        return new ResponseEntity<>(tagModels, HttpStatus.OK);
    }

    @JsonView(TagModel.Views.V1.class)
    @GetMapping(path = "/tag", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagModel> getTheMostUsedTag() {
        Tag tag = tagService.getTheMostUsedTag();
        TagModel tagModel = TagModel.createForm(tag);

        return new ResponseEntity<>(tagModel, HttpStatus.OK);
    }
}
