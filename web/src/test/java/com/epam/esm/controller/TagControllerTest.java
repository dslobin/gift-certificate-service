package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebMvcTest(TagController.class)
class TagControllerTest {
    @MockBean
    private TagService tagService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static final int PAGE = 1;
    private static final int SIZE = 5;
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_SIZE = "size";

    @Test
    void givenTags_whenGetAll_thenGetCorrectCount() throws Exception {
        TagDto tagActiveRest = new TagDto(1L, "active_rest");
        TagDto tagRomantic = new TagDto(2L, "romantic");
        TagDto tagMotorists = new TagDto(3L, "motorists");
        Set<TagDto> tags = Stream.of(tagActiveRest, tagRomantic, tagMotorists)
                .collect(Collectors.toSet());

        when(tagService.findAll(PAGE, SIZE)).thenReturn(tags);

        mockMvc.perform(get("/api/tags")
                .param(PARAM_PAGE, String.valueOf(PAGE))
                .param(PARAM_SIZE, String.valueOf(SIZE)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(3)));
    }

    @Test
    void givenTag_whenGetTagById_thenReturnCorrectTag() throws Exception {
        long tagId = 1;
        String tagName = "sport";
        TagDto tagSport = new TagDto(tagId, tagName);

        when(tagService.findById(tagId)).thenReturn(tagSport);

        mockMvc.perform(get("/api/tags/{id}", tagId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
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
                .andExpect(jsonPath("$.message", Matchers.hasSize(1)));
    }

    @Test
    void givenTag_whenCreate_thenReturnCreatedTagDto() throws Exception {
        long tagId = 1;
        String tagName = "romantics";

        TagDto tagRomantics = new TagDto(tagId, tagName);

        when(tagService.create(tagRomantics)).thenReturn(tagRomantics);

        mockMvc.perform(post("/api/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagRomantics)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(tagId), Long.class))
                .andExpect(jsonPath("$.name", Matchers.is(tagName)));
    }
}
