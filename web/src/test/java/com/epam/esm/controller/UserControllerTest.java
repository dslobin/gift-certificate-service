package com.epam.esm.controller;

import com.epam.esm.dto.RoleDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.service.UserService;
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

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebMvcTest(UserController.class)
class UserControllerTest {
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static final int PAGE = 1;
    private static final int SIZE = 5;
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_SIZE = "size";

    @Test
    void givenUsers_whenGetAll_thenGetCorrectCount() throws Exception {
        Set<RoleDto> roles = Stream.of(
                new RoleDto(1L, "USER")
        ).collect(Collectors.toSet());

        List<UserDto> users = Stream.of(
                new UserDto(1L, "jared.mccarthy@mail.com", "123456", roles),
                new UserDto(2L, "hanna.robbins@mail.com", "123456", roles),
                new UserDto(3L, "emilia.malone@mail.com", "123456", roles)
        ).collect(Collectors.toList());

        when(userService.findAll(PAGE, SIZE)).thenReturn(users);

        mockMvc.perform(get("/api/users")
                .param(PARAM_PAGE, String.valueOf(PAGE))
                .param(PARAM_SIZE, String.valueOf(SIZE)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(3)));
    }

    @Test
    void givenUserId_whenGetUserById_thenReturnCorrectUser() throws Exception {
        Set<RoleDto> roles = Stream.of(
                new RoleDto(1L, "USER"),
                new RoleDto(2L, "ADMIN")
        ).collect(Collectors.toSet());

        UserDto userDto = new UserDto(1L, "jared.mccarthy@mail.com", "123456", roles);

        when(userService.findById(any(Long.class))).thenReturn(userDto);

        long userId = userDto.getId();
        String userEmail = userDto.getEmail();
        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(userId), Long.class))
                .andExpect(jsonPath("$.email", Matchers.is(userEmail)));
    }
}
