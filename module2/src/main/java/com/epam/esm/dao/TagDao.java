package com.epam.esm.dao;

import com.epam.esm.domain.Tag;

import java.util.Optional;

public interface TagDao {
    Optional<Tag> getTagById(Long id);
    void deleteTag(Long id);
}
