package com.epam.esm.service;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Tag;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.service.mapper.TagDtoMapper;
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
    private static Logger log = LogManager.getLogger(TagServiceImpl.class);

    @Autowired
    public TagServiceImpl(TagDao tagDao, TagDtoMapper mapper) {
        this.tagDao = tagDao;
        this.mapper = mapper;
    }

    public List<TagDto> getAllTags() {
        log.debug("Get all tags");
        return mapper.toTagDtoList(tagDao.getAllTags());
    }

    @Override
    public TagDto getTagById(Long id) {
        log.debug(String.format("Get tag with id: %d", id));
        Optional<Tag> optionalTag = tagDao.getTagById(id);
        return mapper.toTagDto(optionalTag.orElseThrow(() -> new TagNotFoundException("Tag with id " + id + " isn't found")));
    }

    @Override
    @Transactional
    public TagDto createTag(Tag tag) {
        log.debug(String.format("Create tag: %s", tag));
        return mapper.toTagDto(tagDao.createTag(tag));
    }

    @Override
    public void deleteTag(Long id) {
        log.debug(String.format("Delete tag with id: %d", id));
        getTagById(id);
        tagDao.deleteTag(id);
    }
}
