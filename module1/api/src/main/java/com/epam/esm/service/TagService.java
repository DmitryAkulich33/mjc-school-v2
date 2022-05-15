package com.epam.esm.service;

import com.epam.esm.domain.Tag;
import com.epam.esm.model.dto.TagDto;

import java.util.List;

public interface TagService {
    List<TagDto> getAllTags();

    TagDto getTagById(Long id);

    TagDto createTag(Tag tag);

    void deleteTag(Long id);
}
