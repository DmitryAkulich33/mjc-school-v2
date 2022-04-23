package com.epam.esm.service;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Tag;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.service.mapper.TagDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
    private final TagDao tagDao;
    private final TagDtoMapper mapper;

    @Autowired
    public TagServiceImpl(TagDao tagDao, TagDtoMapper mapper) {
        this.tagDao = tagDao;
        this.mapper = mapper;
    }

    public List<TagDto> getAllTags() {
        return mapper.toTagDtoList(tagDao.getAllTags());
    }

    @Override
    public TagDto getTagById(Long id) {
        Optional<Tag> optionalTag = tagDao.getTagById(id);
        return mapper.toTagDto(optionalTag.orElseThrow(() -> new TagNotFoundException("Tag with id " + id + " isn't found")));
    }

    @Override
    public TagDto createTag(Tag tag) {
        return mapper.toTagDto(tagDao.createTag(tag));
    }

    @Override
    public void deleteTag(Long id) {
        getTagById(id);
        tagDao.deleteTag(id);
    }
}
