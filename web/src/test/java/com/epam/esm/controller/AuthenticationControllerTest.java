package com.epam.esm.controller;

import com.epam.esm.dto.AuthResponse;
import com.epam.esm.dto.LoginRequest;
import com.epam.esm.dto.RoleDto;
import com.epam.esm.dto.SignUpRequest;
import com.epam.esm.entity.Cart;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.security.AuthenticationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {
    @MockBean
    private AuthenticationService authenticationService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenLoginRequest_whenLogin_thenGetAuthenticationResponse()
            throws Exception {
        String userEmail = "admin@mail.com";
        String password = "123456";
        LoginRequest loginRequest = new LoginRequest(userEmail, password);

        Set<RoleDto> roles = Stream.of(
                new RoleDto(1L, "ROLE_USER")
        ).collect(Collectors.toSet());
        AuthResponse authResponse = new AuthResponse(userEmail, "secret-token", roles);

        when(authenticationService.authenticate(userEmail, password)).thenReturn(authResponse);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.login", Matchers.is(userEmail)));
    }

    @Test
    void givenSignUpRequest_whenRegisterUser_thenGetCreatedUser() throws Exception {
        Set<Role> roles = Stream.of(
                new Role(1L, "USER", null)
        ).collect(Collectors.toSet());
        String userEmail = "jared.mccarthy@mail.com";
        String password = "123456";
        String firstName = "Jared";
        String lastName = "Mccarthy";
        User user = new User(1L, userEmail, password, firstName, lastName, null, roles, true);
        Cart cart = new Cart(user);
        user.setCart(cart);

        SignUpRequest signUpRequest = new SignUpRequest(userEmail, password, firstName, lastName);
        when(authenticationService.signUp(signUpRequest)).thenReturn(user);

        mockMvc.perform(post("/api/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(1L), Long.class))
                .andExpect(jsonPath("$.email", Matchers.is(userEmail)));
    }
}
