package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.NameAlreadyExistException;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.mapper.TagMapper;
import com.epam.esm.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
@Validated
@Slf4j
public class TagController {
    private final TagService tagService;
    private final TagMapper tagMapper;

    @Value("${pagination.defaultPageValue}")
    private Integer defaultPage;
    @Value("${pagination.maxElementsOnPage}")
    private Integer maxElementsOnPage;

    /**
     * View tags.
     *
     * @return tags list
     */
    @GetMapping
    public ResponseEntity<Set<TagDto>> getTags(
            @RequestParam(required = false, defaultValue = "${pagination.defaultPageValue}") Integer page,
            @Min(5) @Max(100) @RequestParam(required = false, defaultValue = "${pagination.maxElementsOnPage}") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Set<TagDto> tags = tagService.findAll(pageable).stream()
                .map(tagMapper::toDto)
                .collect(Collectors.toSet());
        tags.forEach(tagDto -> tagDto.add(linkTo(methodOn(TagController.class).getTag(tagDto.getId())).withSelfRel()));
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
        Tag searchedTag = tagService.findById(id);
        TagDto tagDto = tagMapper.toDto(searchedTag);
        tagDto.add(
                linkTo(methodOn(TagController.class).getTags(defaultPage, maxElementsOnPage)).withRel("tags"),
                linkTo(methodOn(TagController.class).getTag(tagDto.getId())).withSelfRel()
        );
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
        Tag tag = tagService.findById(id);
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
    public ResponseEntity<TagDto> createTag(@Valid @RequestBody TagDto tagDto)
            throws NameAlreadyExistException {
        Tag createdTag = tagService.create(tagMapper.toModel(tagDto));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(tagMapper.toDto(createdTag));
    }
}
