package com.epam.esm.mapper;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TagMapper {
    @Mapping(target = "certificates", ignore = true)
    Tag toModel(TagDto tagDto);

    TagDto toDto(Tag tag);
}
