package com.epam.esm.controller;

import com.epam.esm.config.TestControllerContext;
import com.epam.esm.config.WebMvcConfig;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.impl.TagServiceImpl;
import com.epam.esm.util.TestUtil;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = {TestControllerContext.class, WebMvcConfig.class})
@WebAppConfiguration
class TagControllerTest {
    private MockMvc mockMvc;
    private TagService tagService;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        tagService = mock(TagServiceImpl.class);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void findAll_TagsFound_ShouldReturnFoundTagsEntries() throws Exception {
        Tag tagActiveRest = new Tag(1L, "Active_rest");
        Tag tagRomantic = new Tag(2L, "Romantic");
        Tag tagMotorists = new Tag(3L, "Motorists");
        Set<Tag> tags = Stream.of(tagActiveRest, tagRomantic, tagMotorists)
                .collect(Collectors.toSet());

        given(tagService.findAll()).willReturn(tags);

        MvcResult result = mockMvc.perform(get("/api/tags"))
                .andDo(print())
                .andReturn();

        mockMvc.perform(get("/api/tags"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[0].getId", Matchers.is(1)))
                .andExpect(jsonPath("$[0].getName", Matchers.is("Active_rest")))
                .andExpect(jsonPath("$[1].getId", Matchers.is(2)))
                .andExpect(jsonPath("$[1].getName", Matchers.is("Romantic")))
                .andExpect(jsonPath("$[2].getId", Matchers.is(3)))
                .andExpect(jsonPath("$[2].getName", Matchers.is("Motorists")));

        verify(tagService, times(1)).findAll();
        verifyNoMoreInteractions(tagService);
    }
}
