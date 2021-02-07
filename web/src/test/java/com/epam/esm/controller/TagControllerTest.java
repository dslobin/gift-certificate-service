package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
class TagControllerTest {
    @MockBean
    private TagService tagService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static final String PARAM_PAGE = "page";
    private static final String PARAM_SIZE = "size";

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    void givenTags_whenGetAll_thenGetCorrectCount() throws Exception {
        Tag tagActiveRest = new Tag(1L, "active_rest", null);
        Tag tagRomantic = new Tag(2L, "romantic", null);
        Tag tagMotorists = new Tag(3L, "motorists", null);
        Set<Tag> tags = Stream.of(tagActiveRest, tagRomantic, tagMotorists).collect(Collectors.toSet());

        int page = 0;
        int size = 100;
        PageRequest pageable = PageRequest.of(page, size);

        when(tagService.findAll(pageable)).thenReturn(tags);

        mockMvc.perform(get("/api/tags")
                .param(PARAM_PAGE, String.valueOf(page))
                .param(PARAM_SIZE, String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(3)));
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    void givenTag_whenGetTagById_thenReturnCorrectTag() throws Exception {
        long tagId = 1;
        String tagName = "sport";
        Tag tagSport = new Tag(tagId, tagName, null);

        when(tagService.findById(tagId)).thenReturn(tagSport);

        mockMvc.perform(get("/api/tags/{id}", tagId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(tagId), Long.class))
                .andExpect(jsonPath("$.name", Matchers.is(tagName)));
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    void givenTagWithShortName_whenCreate_thenReturnValidationErrorsForName() throws Exception {
        long tagId = 1;
        String tagName = "no";
        TagDto notValidTag = new TagDto(tagId, tagName);

        mockMvc.perform(post("/api/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notValidTag)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", Matchers.hasSize(1)));
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    void givenTag_whenCreate_thenReturnCreatedTagDto() throws Exception {
        long tagId = 1;
        String tagName = "romantics";

        Tag tagRomantics = new Tag(tagId, tagName, null);

        when(tagService.create(tagRomantics)).thenReturn(tagRomantics);

        mockMvc.perform(post("/api/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagRomantics)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(tagId), Long.class))
                .andExpect(jsonPath("$.name", Matchers.is(tagName)));
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    void givenTagId_whenDelete_thenReturnHttpStatusCode204() throws Exception {
        long tagId = 1;
        String tagName = "romantics";
        Tag tagRomantics = new Tag(tagId, tagName, null);

        when(tagService.findById(any(Long.class))).thenReturn(tagRomantics);

        mockMvc.perform(delete("/api/tags/{id}", String.valueOf(tagId))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
