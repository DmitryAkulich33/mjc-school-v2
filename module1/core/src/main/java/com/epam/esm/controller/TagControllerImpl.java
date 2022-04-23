package com.epam.esm.controller;

import com.epam.esm.domain.Tag;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TagControllerImpl implements TagController {
    private final TagService tagService;

    @Autowired
    public TagControllerImpl(TagService tagService) {
        this.tagService = tagService;
    }

    @Override
    public ResponseEntity<List<TagDto>> getAllTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }

    @Override
    public ResponseEntity<TagDto> getTagById(Long id) {
        return ResponseEntity.ok(tagService.getTagById(id));
    }

    @Override
    public ResponseEntity<TagDto> createTag(Tag tag) {
        return ResponseEntity.ok(tagService.createTag(tag));
    }

    @Override
    public ResponseEntity<TagDto> deleteTag(Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.ok().build();
    }
}
