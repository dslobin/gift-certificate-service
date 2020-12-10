package com.epam.esm.service.impl;

import com.epam.esm.entity.*;
import com.epam.esm.exception.EmptyCartException;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.service.CartService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.config.ServiceContextTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = ServiceContextTest.class)
class OrderServiceImplTest {
    @Autowired
    private OrderRepository orderRepository;
    @MockBean
    private UserService userService;
    @MockBean
    private CartService cartService;
    @Autowired
    private OrderService orderService;

    @Test
    void givenOrders_whenFindAllByUserEmail_thenGetCorrectOrdersSize() {
        Set<Role> roles = Stream.of(new Role(1L, "USER", null)).collect(Collectors.toSet());
        User user = new User(1L, "jared.mccarthy@mail.com", "123456", "Jared", "Mccarthy", null, roles, true);
        List<OrderItem> orderItems = new ArrayList<>();
        ZonedDateTime createDate = ZonedDateTime.now();
        List<Order> orders = Stream.of(
                new Order(1L, BigDecimal.TEN, orderItems, createDate, createDate, user)
        ).collect(Collectors.toList());

        String userEmail = "jared.mccarthy@mail.com";
        int page = 0;
        int size = Integer.MAX_VALUE;
        PageRequest pageable = PageRequest.of(page, size);
        doReturn(user).when(userService).findByEmail(userEmail);
        doReturn(orders).when(orderRepository).findByUserEmail(userEmail, pageable);

        List<Order> actualOrders = orderService.findUserOrders(userEmail, pageable);
        int expectedOrdersSize = orders.size();
        int actualOrdersSize = actualOrders.size();
        assertEquals(expectedOrdersSize, actualOrdersSize);
    }

    @Test
    void givenOrders_whenFindOneByUserEmailAndOrderId_thenGetCorrectOrder() {
        Set<Role> roles = Stream.of(new Role(1L, "USER", null)).collect(Collectors.toSet());
        User user = new User(1L, "jared.mccarthy@mail.com", "123456", "Jared", "Mccarthy", null, roles, true);
        List<OrderItem> orderItems = new ArrayList<>();
        ZonedDateTime createDate = ZonedDateTime.now();
        Order order = new Order(1L, BigDecimal.TEN, orderItems, createDate, createDate, user);

        String userEmail = "jared.mccarthy@mail.com";
        long orderId = 1L;
        doReturn(Optional.of(order)).when(orderRepository).findByIdAndUserEmail(orderId, userEmail);

        Order actualOrder = orderService.findUserOrder(userEmail, orderId);
        assertThat(actualOrder).isNotNull();
        assertEquals(order.getId(), actualOrder.getId());
    }

    @Test
    void givenUserEmail_whenCreateOrder_thenGetCorrectOrder() {
        Set<Role> roles = Stream.of(new Role(1L, "USER", null)).collect(Collectors.toSet());
        User user = new User(1L, "jared.mccarthy@mail.com", "123456", "Jared", "Mccarthy", null, roles, true);
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

        doReturn(user).when(userService).findByEmail(userEmail);
        doReturn(cart).when(cartService).getCartOrCreate(userEmail);
        doReturn(order).when(orderRepository).save(order);
        Cart emptyCart = new Cart(user);
        doReturn(emptyCart).when(cartService).clearCart(userEmail);

        Order createdOrder = orderService.createUserOrder(userEmail);
        assertThat(createdOrder).isNotNull();
        int noItems = 0;
        assertEquals(noItems, emptyCart.getItemsCount());
    }

    @Test
    void givenUserEmail_whenCreateOrder_thenThrowEmptyCartException() {
        Set<Role> roles = Stream.of(new Role(1L, "USER", null)).collect(Collectors.toSet());
        User user = new User(1L, "jared.mccarthy@mail.com", "123456", "Jared", "Mccarthy", null, roles, true);
        Cart cart = new Cart(user);
        cart.setId(user.getId());

        String userEmail = "jared.mccarthy@mail.com";

        when(userService.findByEmail(userEmail)).thenReturn(user);
        when(cartService.getCartOrCreate(userEmail)).thenReturn(cart);
        when(cartService.isCartEmpty(cart)).thenReturn(true);

        assertThrows(EmptyCartException.class, () -> {
            orderService.createUserOrder(userEmail);
        });
    }
}
