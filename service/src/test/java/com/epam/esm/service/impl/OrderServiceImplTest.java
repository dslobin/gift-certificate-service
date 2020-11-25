package com.epam.esm.service.impl;

import com.epam.esm.dao.CartDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.*;
import com.epam.esm.exception.EmptyCartException;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.config.ServiceContextTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = ServiceContextTest.class)
class OrderServiceImplTest {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private CartDao cartDao;
    @Autowired
    private OrderService orderService;

    @Test
    void givenOrders_whenFindAllByUserEmail_thenGetCorrectOrdersSize() {
        Set<Role> roles = Stream.of(new Role(1L, "USER", null)).collect(Collectors.toSet());
        User user = new User(1L, "jared.mccarthy@mail.com", "123456", "Jared", "Mccarthy", null, roles);
        List<OrderItem> orderItems = new ArrayList<>();
        ZonedDateTime createDate = ZonedDateTime.now();
        List<Order> orders = Stream.of(
                new Order(1L, BigDecimal.TEN, orderItems, createDate, createDate, user)
        ).collect(Collectors.toList());

        String userEmail = "jared.mccarthy@mail.com";
        int page = 1;
        int size = 5;
        doReturn(Optional.of(user)).when(userDao).findByEmail(userEmail);
        doReturn(orders).when(orderDao).findByUserEmail(page, size, userEmail);

        List<OrderDto> actualOrders = orderService.findUserOrders(page, size, userEmail);
        int expectedOrdersSize = orders.size();
        int actualOrdersSize = actualOrders.size();
        assertEquals(expectedOrdersSize, actualOrdersSize);
    }

    @Test
    void givenOrders_whenFindOneByUserEmailAndOrderId_thenGetCorrectOrder() {
        Set<Role> roles = Stream.of(new Role(1L, "USER", null)).collect(Collectors.toSet());
        User user = new User(1L, "jared.mccarthy@mail.com", "123456", "Jared", "Mccarthy", null, roles);
        List<OrderItem> orderItems = new ArrayList<>();
        ZonedDateTime createDate = ZonedDateTime.now();
        Order order = new Order(1L, BigDecimal.TEN, orderItems, createDate, createDate, user);

        String userEmail = "jared.mccarthy@mail.com";
        long orderId = 1L;
        doReturn(Optional.of(order)).when(orderDao).findByIdAndUserEmail(orderId, userEmail);

        OrderDto actualOrder = orderService.findUserOrder(userEmail, orderId);
        assertThat(actualOrder).isNotNull();
        assertEquals(order.getId(), actualOrder.getOrderId());
    }

    @Test
    void givenUserEmail_whenCreateOrder_thenGetCorrectOrder() {
        Set<Role> roles = Stream.of(new Role(1L, "USER", null)).collect(Collectors.toSet());
        User user = new User(1L, "jared.mccarthy@mail.com", "123456", "Jared", "Mccarthy", null, roles);
        List<OrderItem> orderItems = new ArrayList<>();
        ZonedDateTime createDate = ZonedDateTime.now();
        GiftCertificate certificate = new GiftCertificate(
                1L,
                "Culinary master class Italian cuisine",
                "A culinary master class is an opportunity to become a Chef of a real Italian restaurant for 3 hours.",
                BigDecimal.valueOf(95.00),
                ZonedDateTime.now(),
                ZonedDateTime.now().plusDays(3),
                Duration.ofDays(14),
                new HashSet<>(),
                true
        );
        Cart cart = new Cart(user);
        cart.setId(user.getId());
        String userEmail = "jared.mccarthy@mail.com";
        Set<CartItem> cartItems = Stream.of(
                new CartItem(cart, certificate, 1)
        ).collect(Collectors.toSet());
        cart.setItems(cartItems);
        Order order = new Order(1L, BigDecimal.TEN, orderItems, createDate, createDate, user);
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setGiftCertificate(certificate);
        orderItem.setQuantity(1);

        doReturn(Optional.of(user)).when(userDao).findByEmail(userEmail);
        doReturn(cart).when(cartDao).findByUserEmail(userEmail);
        doReturn(order).when(orderDao).save(order);
        doReturn(cart).when(cartDao).save(cart);

        OrderDto createdOrder = orderService.createUserOrder(userEmail);
        assertThat(createdOrder).isNotNull();
    }

    @Test
    void givenUserEmail_whenCreateOrder_thenThrowEmptyCartException() {
        Set<Role> roles = Stream.of(new Role(1L, "USER", null)).collect(Collectors.toSet());
        User user = new User(1L, "jared.mccarthy@mail.com", "123456", "Jared", "Mccarthy", null, roles);
        Cart cart = new Cart(user);
        cart.setId(user.getId());

        String userEmail = "jared.mccarthy@mail.com";

        when(userDao.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(cartDao.findByUserEmail(userEmail)).thenReturn(cart);

        assertThrows(EmptyCartException.class, () -> {
            orderService.createUserOrder(userEmail);
        });
    }
}
