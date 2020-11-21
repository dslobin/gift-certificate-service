package com.epam.esm.service;

import com.epam.esm.dto.TagDto;

import java.util.Optional;
import java.util.Set;

public interface TagService {
    /**
     * @param page current page index
     * @param size number of items per page
     * @return tag list
     */
    Set<TagDto> findAll(int page, int size);

    /**
     * @param id unique identifier of the specified tag
     * @return tag associated with the specified id
     */
    Optional<TagDto> findById(long id);

    /**
     * @param name unique identifier of the specified tag
     * @return tag associated with the specified name
     */
    Optional<TagDto> findByName(String name);

    /**
     * Creates new tag.
     *
     * @return created tag
     */
    TagDto create(TagDto tag);

    /**
     * Removes the tag.
     *
     * @param id unique identifier of the specified tag
     */
    void deleteById(long id);
}
