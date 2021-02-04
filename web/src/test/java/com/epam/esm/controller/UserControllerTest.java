package com.epam.esm.controller;

import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.service.UserService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static final String PARAM_PAGE = "page";
    private static final String PARAM_SIZE = "size";

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    void givenUsers_whenGetAll_thenGetCorrectCount() throws Exception {
        Set<Role> roles = Stream.of(
                new Role(1L, "USER", null)
        ).collect(Collectors.toSet());
        Set<User> users = Stream.of(
                new User(1L, "jared.mccarthy@mail.com", "123456", "Jared", "Mccarthy", null, roles, true),
                new User(2L, "hanna.robbins@mail.com", "123456", "Hanna", "Robbins", null, roles, true),
                new User(3L, "emilia.malone@mail.com", "123456", "Emilia", "Malone", null, roles, true)
        ).collect(Collectors.toSet());

        int page = 0;
        int size = 100;
        PageRequest pageable = PageRequest.of(page, size);

        when(userService.findAll(pageable)).thenReturn(users);

        mockMvc.perform(get("/api/users")
                .param(PARAM_PAGE, String.valueOf(page))
                .param(PARAM_SIZE, String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(3)));
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    void givenUserId_whenGetUserById_thenReturnCorrectUser() throws Exception {
        Set<Role> roles = Stream.of(
                new Role(1L, "USER", null)
        ).collect(Collectors.toSet());
        User user = new User(1L, "jared.mccarthy@mail.com", "123456", "Jared", "Mccarthy", null, roles, true);

        when(userService.findById(any(Long.class))).thenReturn(user);

        long userId = user.getId();
        String userEmail = user.getEmail();
        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(userId), Long.class))
                .andExpect(jsonPath("$.email", Matchers.is(userEmail)));
    }
}
