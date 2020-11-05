package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.mapper.TagMapper;
import com.epam.esm.service.TagService;
import com.epam.esm.exception.NameAlreadyExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;
    private final TagMapper tagMapper;

    /**
     * View tags.
     *
     * @return tags list
     */
    @GetMapping
    public ResponseEntity<Set<TagDto>> getAllTags() {
        Set<TagDto> tags = tagService.findAll().stream()
                .map(tagMapper::toDto)
                .collect(Collectors.toSet());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tags);
    }

    /**
     * Viewing a single tag.
     *
     * @return tag with the specified id
     * @throws TagNotFoundException if the tag with the specified id doesn't exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<TagDto> getOneTag(@PathVariable Long id) {
        Tag tag = tagService.findById(id)
                .orElseThrow(() -> new TagNotFoundException(id));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tagMapper.toDto(tag));
    }

    /**
     * Deleting a tag.
     *
     * @return empty response
     * @throws TagNotFoundException if the specified tag does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        Tag tag = tagService.findById(id)
                .orElseThrow(() -> new TagNotFoundException(id));
        tagService.deleteById(tag.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Adding a tag.
     *
     * @return new tag
     * @throws NameAlreadyExistException if tag with such name already saved in data storage
     */
    @PostMapping
    public ResponseEntity<TagDto> createTag(@Valid @RequestBody TagDto tagDto) {
        boolean isTagNamePresent = tagService.findByName(tagDto.getName()).isPresent();
        if (isTagNamePresent) {
            throw new NameAlreadyExistException(tagDto.getName());
        }
        Tag newTag = tagService.create(tagDto.getName());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(tagMapper.toDto(newTag));
    }
}
