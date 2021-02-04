package com.epam.esm.controller;

import com.epam.esm.dto.RoleDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderItem;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.service.OrderService;
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

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {
    @MockBean
    private OrderService orderService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static final String PARAM_PAGE = "page";
    private static final String PARAM_SIZE = "size";

    /*@Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    void givenOrders_whenGetAllByUserEmail_thenGetCorrectCount() throws Exception {
        Set<Role> roles = Stream.of(new Role(1L, "USER", null)).collect(Collectors.toSet());
        User user = new User(1L, "jared.mccarthy@mail.com", "123456", "Jared", "Mccarthy", null, roles, true);
        List<OrderItem> orderItems = new ArrayList<>();
        ZonedDateTime createDate = ZonedDateTime.now();
        List<Order> orders = Stream.of(
                new Order(1L, BigDecimal.TEN, orderItems, createDate, createDate, user)
        ).collect(Collectors.toList());

        int page = 0;
        int size = 100;
        PageRequest pageable = PageRequest.of(page, size);

        when(orderService.findUserOrders("felix.ryan@mail.com", pageable)).thenReturn(orders);

        Set<RoleDto> roleDtos = Stream.of(
                new RoleDto(1L, "USER")
        ).collect(Collectors.toSet());
        UserDto userDto = new UserDto(1L, "felix.ryan@mail.com", "123456", roleDtos);
        mockMvc.perform(get("/api/orders")
                .param(PARAM_PAGE, String.valueOf(page))
                .param(PARAM_SIZE, String.valueOf(size))
                .content(objectMapper.writeValueAsString(userDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(1)));
    }*/

    // TODO: 17.01.2021 переделать тесты
    /*@Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    void givenOrder_whenGetOneByUserEmail_thenGetCorrectOrder() throws Exception {
        Set<Role> roles = Stream.of(new Role(1L, "USER", null)).collect(Collectors.toSet());
        User user = new User(1L, "jared.mccarthy@mail.com", "123456", "Jared", "Mccarthy", null, roles, true);
        List<OrderItem> orderItems = new ArrayList<>();
        ZonedDateTime createDate = ZonedDateTime.now();
        Order order = new Order(1L, BigDecimal.TEN, orderItems, createDate, createDate, user);

        when(orderService.findUserOrder("felix.ryan@mail.com", 1L)).thenReturn(order);

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
    }*/

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    void givenOrder_whenCreate_thenReturnCreatedOrderDto() throws Exception {
        Set<Role> roles = Stream.of(new Role(1L, "USER", null)).collect(Collectors.toSet());
        String userEmail = "jared.mccarthy@mail.com";
        User user = new User(1L, userEmail, "123456", "Jared", "Mccarthy", null, roles, true);
        List<OrderItem> orderItems = new ArrayList<>();
        ZonedDateTime createDate = ZonedDateTime.now();
        long orderId = 1L;
        Order order = new Order(orderId, BigDecimal.TEN, orderItems, createDate, createDate, user);

        when(orderService.createUserOrder(anyString())).thenReturn(order);

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
