package com.epam.esm.controller;

import com.epam.esm.dto.CartDto;
import com.epam.esm.dto.CartItemDto;
import com.epam.esm.dto.RoleDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.service.CartService;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebMvcTest(CartController.class)
class CartControllerTest {
    @MockBean
    private CartService cartService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenUserDto_whenGetCart_thenGetUserCart() throws Exception {
        CartDto cartDto = new CartDto("jared.mccarthy@mail.com", new ArrayList<>(), 0, BigDecimal.ZERO);
        when(cartService.getCartOrCreate(any(String.class))).thenReturn(cartDto);

        Set<RoleDto> roleDtos = Stream.of(
                new RoleDto(1L, "USER")
        ).collect(Collectors.toSet());
        UserDto userDto = new UserDto(1L, "jared.mccarthy@mail.com", "123456", roleDtos);

        String userEmail = cartDto.getUserEmail();
        mockMvc.perform(get("/api/cart")
                .content(objectMapper.writeValueAsString(userDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.userEmail", Matchers.is(userEmail)));
    }

    @Test
    void givenCartItemDto_whenAddToCart_thenGetUpdatedCart() throws Exception {
        CartItemDto itemDto1 = new CartItemDto(1, 1, "jared.mccarthy@mail.com");
        CartItemDto itemDto2 = new CartItemDto(2, 1, "jared.mccarthy@mail.com");
        List<CartItemDto> cartItemDtos = Arrays.asList(itemDto1, itemDto2);
        CartDto cartDto = new CartDto("jared.mccarthy@mail.com", cartItemDtos, 3, BigDecimal.valueOf(100));
        when(cartService.addToCart(any(String.class), any(Long.class), any(Integer.class))).thenReturn(cartDto);


        String userEmail = cartDto.getUserEmail();
        mockMvc.perform(put("/api/cart")
                .content(objectMapper.writeValueAsString(itemDto1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.userEmail", Matchers.is(userEmail)));
    }

    @Test
    void givenUserDto_whenClearCart_thenGetClearedUserCart() throws Exception {
        CartDto cartDto = new CartDto("jared.mccarthy@mail.com", new ArrayList<>(), 0, BigDecimal.ZERO);
        when(cartService.clearCart(any(String.class))).thenReturn(cartDto);

        Set<RoleDto> roleDtos = Stream.of(
                new RoleDto(1L, "USER")
        ).collect(Collectors.toSet());
        UserDto userDto = new UserDto(1L, "jared.mccarthy@mail.com", "123456", roleDtos);

        String userEmail = cartDto.getUserEmail();
        mockMvc.perform(delete("/api/cart")
                .content(objectMapper.writeValueAsString(userDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.userEmail", Matchers.is(userEmail)));
    }
}
