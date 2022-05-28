package com.epam.esm.service;

import com.epam.esm.domain.Tag;

import java.util.List;

public interface TagService {
    Tag getTagById(Long id);
    void deleteTag(Long id);
    Tag createTag(Tag tag);
    List<Tag> getTags(Integer pageNumber, Integer pageSize);
}
