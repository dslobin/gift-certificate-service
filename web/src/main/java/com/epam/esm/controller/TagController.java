package com.epam.esm.controller;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping
    public ResponseEntity<Set<Tag>> getAllTags() {
        Set<Tag> tags = tagService.findAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tags);
    }

    @DeleteMapping("/${id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        Tag tag = tagService.findById(id)
                .orElseThrow(() -> new TagNotFoundException(id));
        tagService.deleteById(tag.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag) {
        Tag newTag = tagService.create(tag);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newTag);
    }
}
