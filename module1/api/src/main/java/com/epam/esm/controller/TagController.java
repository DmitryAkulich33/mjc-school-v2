package com.epam.esm.controller;

import com.epam.esm.domain.Tag;
import com.epam.esm.model.dto.TagDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

@Validated
@RequestMapping(value = "/tags")
public interface TagController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<TagDto>> getAllTags();

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<TagDto> getTagById(@PathVariable("id") @Positive Long id);

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<TagDto> createTag(@RequestBody Tag tag);

    @DeleteMapping(path = "/{id}")
    ResponseEntity<TagDto> deleteTag(@PathVariable("id") @Positive Long id);
}
