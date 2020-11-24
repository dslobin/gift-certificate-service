package com.epam.esm.service;

import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.NameAlreadyExistException;
import com.epam.esm.exception.TagNotFoundException;

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
     * @throws TagNotFoundException if the specified tag does not exist
     */
    TagDto findById(long id);

    /**
     * @param name unique identifier of the specified tag
     * @return tag associated with the specified name
     * @throws TagNotFoundException if the specified tag does not exist
     */
    TagDto findByName(String name);

    /**
     * Creates new tag.
     *
     * @return created tag
     * @throws NameAlreadyExistException if the specified tag with such name already exist
     */
    TagDto create(TagDto tag);

    /**
     * Removes the tag.
     *
     * @param id unique identifier of the specified tag
     * @throws TagNotFoundException if the specified tag does not exist
     */
    void deleteById(long id);
}
