package com.epam.esm.controller;

import com.epam.esm.config.ControllerContextTest;
import com.epam.esm.config.SpringApplicationInitializer;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.service.TagService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = {
        ControllerContextTest.class,
        SpringApplicationInitializer.class,
})
@WebAppConfiguration
class TagControllerTest {
    @Autowired
    private TagService tagService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        Mockito.reset(tagService);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void givenTags_whenGetAll_thenGetCorrectCount() throws Exception {
        Tag tagActiveRest = new Tag(1L, "Active_rest");
        Tag tagRomantic = new Tag(2L, "Romantic");
        Tag tagMotorists = new Tag(3L, "Motorists");
        Set<Tag> tags = Stream.of(tagActiveRest, tagRomantic, tagMotorists).collect(Collectors.toSet());

        when(tagService.findAll()).thenReturn(tags);

        mockMvc.perform(get("/api/tags"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(3)));
    }

    @Test
    void givenTagId_whenGetTagById_thenReturnHttpStatusCode404() throws Exception {
        long tagId = 1L;
        when(tagService.findById(tagId)).thenThrow(new TagNotFoundException(tagId));

        mockMvc.perform(get("/api/tags/{id}", tagId))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenTag_whenGetTagById_thenReturnCorrectTag() throws Exception {
        long tagId = 1;
        String tagName = "Sport";
        Tag tagSport = new Tag(tagId, tagName);

        when(tagService.findById(tagId)).thenReturn(Optional.of(tagSport));

        mockMvc.perform(get("/api/tags/{id}", tagId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(tagId), Long.class))
                .andExpect(jsonPath("$.name", Matchers.is(tagName)));
    }

    @Test
    void givenTagWithShortName_whenCreate_thenReturnValidationErrorsForName() throws Exception {
        long tagId = 1;
        String tagName = "no";
        TagDto notValidTag = new TagDto(tagId, tagName);

        mockMvc.perform(post("/api/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notValidTag)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
    }

    @Test
    void givenTag_whenCreate_thenReturnCreatedTagDto() throws Exception {
        long tagId = 1;
        String tagName = "Romantics";

        Tag tagRomantics = new Tag(tagId, tagName);
        TagDto tagRomanticsDto = new TagDto(tagId, tagName);

        when(tagService.create(any(String.class))).thenReturn(tagRomantics);

        mockMvc.perform(post("/api/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagRomanticsDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(tagId), Long.class))
                .andExpect(jsonPath("$.name", Matchers.is(tagName)));
    }
}
