package com.epam.esm.service.mapper;

import com.epam.esm.domain.Tag;
import com.epam.esm.model.dto.TagDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagDtoMapper {
    List<TagDto> toTagDtoList(List<Tag> tags);
    TagDto toTagDto(Tag tag);
}
