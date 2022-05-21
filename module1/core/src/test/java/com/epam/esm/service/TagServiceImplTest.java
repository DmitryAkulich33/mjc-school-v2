package com.epam.esm.service;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Tag;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.service.mapper.TagDtoMapper;
import com.epam.esm.service.validator.TagValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {
    @Mock
    private TagDao mockTagDao;
    @Mock
    private TagValidator mockTagValidator;
    @Mock
    private TagDtoMapper mockTagDtoMapper;

    private Tag tag = new Tag();
    private TagDto tagDto = new TagDto();

    @InjectMocks
    private TagServiceImpl tagServiceImpl;

    @Test
    public void testGetTagById() {
        when(mockTagDao.getTagById(anyLong())).thenReturn(Optional.of(tag));
        when(mockTagDtoMapper.toTagDto(tag)).thenReturn(tagDto);

        TagDto actual = tagServiceImpl.getTagById(anyLong());

        assertEquals(tagDto, actual);
    }

    @Test
    public void testGetTagById_TagNotFoundException() {
        when(mockTagDao.getTagById(anyLong())).thenReturn(Optional.empty());

        assertThrows(TagNotFoundException.class, () -> {
            tagServiceImpl.getTagById(anyLong());
        });
    }

    @Test
    public void testDeleteTagById() {
        when(mockTagDao.getTagById(anyLong())).thenReturn(Optional.of(tag));

        tagServiceImpl.deleteTag(anyLong());

        verify(mockTagDao).getTagById(anyLong());
        verify(mockTagDao).deleteTag(anyLong());
    }

    @Test
    public void testDeleteTagById_TagNotFoundException() {
        when(mockTagDao.getTagById(anyLong())).thenReturn(Optional.empty());

        assertThrows(TagNotFoundException.class, () -> {
            tagServiceImpl.deleteTag(anyLong());
            ;
        });
    }

    @Test
    public void testGetAllTags() {
        List<Tag> tags = new ArrayList<>();
        List<TagDto> expected = new ArrayList<>();

        when(mockTagDao.getAllTags()).thenReturn(tags);

        List<TagDto> actual = tagServiceImpl.getAllTags();

        assertEquals(expected, actual);
    }

    @Test
    public void testCreateTag() {
        when(mockTagDao.createTag(any(Tag.class))).thenReturn(tag);
        when(mockTagDtoMapper.toTagDto(tag)).thenReturn(tagDto);

        TagDto actual = tagServiceImpl.createTag(tag);

        assertEquals(tagDto, actual);
        verify(mockTagValidator).validateTag(tag);
    }
}