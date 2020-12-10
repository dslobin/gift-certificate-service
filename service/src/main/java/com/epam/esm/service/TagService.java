package com.epam.esm.service;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.NameAlreadyExistException;
import com.epam.esm.exception.TagNotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;

public interface TagService {
    /**
     * @param pageable pagination information
     * @return tag list
     */
    Set<Tag> findAll(Pageable pageable);

    /**
     * @param id unique identifier of the specified tag
     * @return tag associated with the specified id
     * @throws TagNotFoundException if the specified tag does not exist
     */
    Tag findById(long id);

    /**
     * @param name unique identifier of the specified tag
     * @return tag associated with the specified name
     * @throws TagNotFoundException if the specified tag does not exist
     */
    Optional<Tag> findByName(String name);

    /**
     * Creates new tag.
     *
     * @return created tag
     * @throws NameAlreadyExistException if the specified tag with such name already exist
     */
    Tag create(Tag tag);

    /**
     * Removes the tag.
     *
     * @param id unique identifier of the specified tag
     * @throws TagNotFoundException if the specified tag does not exist
     */
    void deleteById(long id);
}
