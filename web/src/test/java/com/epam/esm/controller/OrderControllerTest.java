package com.epam.esm.controller;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.RoleDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.service.OrderService;
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
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebMvcTest(OrderController.class)
class OrderControllerTest {
    @MockBean
    private OrderService orderService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static final int PAGE = 1;
    private static final int SIZE = 5;
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_SIZE = "size";

    @Test
    void givenOrders_whenGetAllByUserEmail_thenGetCorrectCount() throws Exception {
        ZonedDateTime createDate = ZonedDateTime.now();
        List<OrderDto> orderDtos = Arrays.asList(
                new OrderDto("felix.ryan@mail.com", "Felix", "Ryan", 1L, createDate, createDate, BigDecimal.valueOf(99)),
                new OrderDto("felix.ryan@mail.com", "Felix", "Ryan", 2L, createDate, createDate, BigDecimal.valueOf(1.99)),
                new OrderDto("felix.ryan@mail.com", "Felix", "Ryan", 3L, createDate, createDate, BigDecimal.valueOf(10000))
        );

        when(orderService.findUserOrders(PAGE, SIZE, "felix.ryan@mail.com")).thenReturn(orderDtos);

        Set<RoleDto> roleDtos = Stream.of(
                new RoleDto(1L, "USER")
        ).collect(Collectors.toSet());
        UserDto userDto = new UserDto(1L, "felix.ryan@mail.com", "123456", roleDtos);
        mockMvc.perform(get("/api/orders")
                .param(PARAM_PAGE, String.valueOf(PAGE))
                .param(PARAM_SIZE, String.valueOf(SIZE))
                .content(objectMapper.writeValueAsString(userDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(3)));
    }

    @Test
    void givenOrder_whenGetOneByUserEmail_thenGetCorrectOrder() throws Exception {
        ZonedDateTime createDate = ZonedDateTime.now();
        OrderDto orderDto = new OrderDto("felix.ryan@mail.com", "Felix", "Ryan", 1L, createDate, createDate, BigDecimal.valueOf(30.01));

        when(orderService.findUserOrder("felix.ryan@mail.com", 1L)).thenReturn(orderDto);

        Set<RoleDto> roleDtos = Stream.of(
                new RoleDto(1L, "USER")
        ).collect(Collectors.toSet());
        UserDto userDto = new UserDto(1L, "felix.ryan@mail.com", "123456", roleDtos);
        long orderId = 1L;
        mockMvc.perform(get("/api/orders/{id}", orderId)
                .content(objectMapper.writeValueAsString(userDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.orderId", Matchers.is(1L), Long.class));
    }

    @Test
    void givenOrder_whenCreate_thenReturnCreatedOrderDto() throws Exception {
        ZonedDateTime createDate = ZonedDateTime.now();
        String userEmail = "felix.ryan@mail.com";
        long orderId = 1L;
        OrderDto orderDto = new OrderDto(userEmail, "Felix", "Ryan", orderId, createDate, createDate, BigDecimal.valueOf(3.14));

        when(orderService.createUserOrder(userEmail)).thenReturn(orderDto);

        Set<RoleDto> roleDtos = Stream.of(
                new RoleDto(1L, "USER")
        ).collect(Collectors.toSet());
        UserDto userDto = new UserDto(1L, userEmail, "123456", roleDtos);
        mockMvc.perform(post("/api/orders/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.orderId", Matchers.is(orderId), Long.class))
                .andExpect(jsonPath("$.userEmail", Matchers.is(userEmail)));
    }
}
