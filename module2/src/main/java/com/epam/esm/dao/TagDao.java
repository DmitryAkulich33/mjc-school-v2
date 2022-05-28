package com.epam.esm.dao;

import com.epam.esm.domain.Tag;

import java.util.List;
import java.util.Optional;

public interface TagDao {
    Optional<Tag> getTagById(Long id);

    void deleteTag(Long id);

    Tag createTag(Tag tag);

    List<Tag> getTags(Integer offset, Integer pageSize);

    List<Tag> getTags();

    Optional<Tag> getTagByName(String name);
}
