package com.epam.esm.dao;

import com.epam.esm.domain.Tag;

import java.util.List;
import java.util.Optional;

public interface TagDao {
    List<Tag> getAllTags();
    Optional<Tag> getTagById(Long id);
    Tag createTag(Tag tag);
    void deleteTag(Long id);
    List<Tag> getTagsFromCertificate(Long id);
}
