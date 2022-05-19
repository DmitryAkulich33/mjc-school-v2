package com.epam.esm.service;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Tag;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.service.mapper.TagDtoMapper;
import com.epam.esm.service.validator.TagValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
    private final TagDao tagDao;
    private final TagDtoMapper mapper;
    private final TagValidator tagValidator;
    private static Logger log = LogManager.getLogger(TagServiceImpl.class);

    @Autowired
    public TagServiceImpl(TagDao tagDao, TagDtoMapper mapper, TagValidator tagValidator) {
        this.tagDao = tagDao;
        this.mapper = mapper;
        this.tagValidator = tagValidator;
    }

    public List<TagDto> getAllTags() {
        log.debug("Get all tags");
        return mapper.toTagDtoList(tagDao.getAllTags());
    }

    @Override
    public TagDto getTagById(Long id) {
        log.debug(String.format("Get tag with id: %d", id));
        Optional<Tag> optionalTag = tagDao.getTagById(id);
        return mapper.toTagDto(optionalTag.orElseThrow(() -> new TagNotFoundException("tag.id.not.found", id)));
    }

    @Override
    @Transactional
    public TagDto createTag(Tag tag) {
        log.debug(String.format("Create tag: %s", tag));
        tagValidator.validateTag(tag);
        return mapper.toTagDto(tagDao.createTag(tag));
    }

    @Override
    public void deleteTag(Long id) {
        log.debug(String.format("Delete tag with id: %d", id));
        getTagById(id);
        tagDao.deleteTag(id);
    }
}
