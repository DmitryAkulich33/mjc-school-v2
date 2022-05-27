package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Tag;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.service.TagService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
    private final TagDao tagDao;

    private static Logger log = LogManager.getLogger(TagServiceImpl.class);

    @Autowired
    public TagServiceImpl(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public Tag getTagById(Long id) {
        log.debug(String.format("Getting tag with id: %d", id));
        Optional<Tag> optionalTag = tagDao.getTagById(id);
        return optionalTag.orElseThrow(() -> new TagNotFoundException("tag.id.not.found", id));
    }

    @Transactional
    @Override
    public void deleteTag(Long id) {
        log.debug(String.format("Removing tag with id:  %d", id));
        getTagById(id);
        tagDao.deleteTag(id);
    }

    @Transactional
    @Override
    public Tag createTag(Tag tag) {
        log.debug(String.format("Creating tag with name:  %s", tag.getName()));
        return tagDao.createTag(tag);
    }
}
