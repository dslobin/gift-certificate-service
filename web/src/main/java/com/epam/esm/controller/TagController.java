package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.NameAlreadyExistException;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
@Validated
public class TagController {
    private final TagService tagService;

    /**
     * View tags.
     *
     * @return tags list
     */
    @GetMapping
    public ResponseEntity<Set<TagDto>> getTags(
            @Min(1) @RequestParam(required = false, defaultValue = "1") Integer page,
            @Min(5) @Max(100) @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        Set<TagDto> tags = tagService.findAll(page, size);
        tags.forEach(tagDto -> tagDto.add(linkTo(methodOn(TagController.class)
                .getTag(tagDto.getId()))
                .withSelfRel()));
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
    public ResponseEntity<TagDto> getTag(@Min(1) @PathVariable Long id)
            throws TagNotFoundException {
        TagDto tagDto = tagService.findById(id);
        tagDto.add(linkTo(methodOn(TagController.class)
                .getTags(1, 10))
                .withRel("tags"));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tagDto);
    }

    /**
     * Deleting a tag.
     *
     * @return empty response
     * @throws TagNotFoundException if the specified tag does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@Min(1) @PathVariable Long id)
            throws TagNotFoundException {
        TagDto tagDto = tagService.findById(id);
        tagService.deleteById(tagDto.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Adding a tag.
     *
     * @return new tag
     * @throws NameAlreadyExistException if tag with such name already saved in data storage
     */
    @PostMapping
    public ResponseEntity<TagDto> createTag(@Valid @RequestBody TagDto tagDto)
            throws NameAlreadyExistException {
        TagDto createdTag = tagService.create(tagDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdTag);
    }
}
