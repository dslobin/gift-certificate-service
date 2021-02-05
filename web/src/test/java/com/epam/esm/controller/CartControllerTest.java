package com.epam.esm.controller;

import com.epam.esm.dto.RoleDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Cart;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.service.CartService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
class CartControllerTest {
    @MockBean
    private CartService cartService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    void givenUserDto_whenGetCart_thenGetUserCart() throws Exception {
        Set<Role> roles = Stream.of(new Role(1L, "USER", null)).collect(Collectors.toSet());
        User user = new User(1L, "jared.mccarthy@mail.com", "123456", "Jared", "Mccarthy", null, roles, true);
        Cart cart = new Cart(user);

        when(cartService.getCartOrCreate(any(String.class))).thenReturn(cart);

        Set<RoleDto> roleDtos = Stream.of(
                new RoleDto(1L, "USER")
        ).collect(Collectors.toSet());
        UserDto userDto = new UserDto(1L, "jared.mccarthy@mail.com", "123456", roleDtos);

        String userEmail = cart.getUser().getEmail();
        mockMvc.perform(get("/api/cart")
                .content(objectMapper.writeValueAsString(userDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.userEmail", Matchers.is(userEmail)));
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    void givenUserDto_whenClearCart_thenGetClearedUserCart() throws Exception {
        Set<Role> roles = Stream.of(new Role(1L, "USER", null)).collect(Collectors.toSet());
        User user = new User(1L, "jared.mccarthy@mail.com", "123456", "Jared", "Mccarthy", null, roles, true);
        Cart cart = new Cart(user);
        when(cartService.clearCart(any(String.class))).thenReturn(cart);

        Set<RoleDto> roleDtos = Stream.of(
                new RoleDto(1L, "USER")
        ).collect(Collectors.toSet());
        UserDto userDto = new UserDto(1L, "jared.mccarthy@mail.com", "123456", roleDtos);

        String userEmail = cart.getUser().getEmail();
        mockMvc.perform(delete("/api/cart")
                .content(objectMapper.writeValueAsString(userDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.userEmail", Matchers.is(userEmail)));
    }
}
