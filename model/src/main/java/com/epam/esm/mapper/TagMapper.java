package com.epam.esm.mapper;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;

public interface TagMapper {
    Tag toModel(TagDto tagDto);

    TagDto toDto(Tag tag);
}
