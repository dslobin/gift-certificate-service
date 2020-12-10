package com.epam.esm.repository;

import com.epam.esm.entity.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;
    @MockBean
    private CartRepository cartRepository;

    @Test
    @Sql(scripts = {"/test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenMultipleOrders_whenFindByUserEmail_thenGetCorrectUserOrders() {
        String userEmail = "jared.mccarthy.admin@mail.com";
        List<Order> userOrders = orderRepository.findByUserEmail(userEmail);

        int expectedOrdersCount = 2;
        assertEquals(expectedOrdersCount, userOrders.size());
    }

    @Test
    @Sql(scripts = {"/test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenMultipleOrders_whenFindByUserEmailAndOrderId_thenGetCorrectUserOrder() {
        String userEmail = "jared.mccarthy.admin@mail.com";
        long orderId = 1;

        Optional<Order> userOrder = orderRepository.findByIdAndUserEmail(orderId, userEmail);

        assertTrue(userOrder.isPresent());

        Order savedOrder = userOrder.get();
        String actualUserEmail = savedOrder.getUser().getEmail();
        assertEquals(userEmail, actualUserEmail);
    }

    @Test
    @Sql(scripts = {"/test-one-user-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenOrder_whenSave_thenGetCorrectUserId() {
        String userEmail = "jared.mccarthy@mail.com";
        Set<Role> roles = Stream.of(new Role(1L, "ROLE_USER", null)).collect(Collectors.toSet());
        User user = new User(1L, userEmail, "123456", "Jared", "Mccarthy", null, roles, true);
        Cart cart = prepareUserCartForOrder(user);

        given(cartRepository.findByUserEmail(userEmail)).willReturn(Optional.of(cart));

        Cart existedCart = cartRepository.findByUserEmail(userEmail).orElse(null);

        assertTrue(Objects.nonNull(existedCart));

        fillCartItemsCost(existedCart);

        Order order = createNewOrder(user, existedCart);
        fillOrderItems(existedCart, order);
        Order createdOrder = orderRepository.save(order);

        given(cartRepository.save(existedCart)).willReturn(getEmptyUserCart(user));
        existedCart = clearCart(existedCart);

        assertTrue(existedCart.getItems().isEmpty());
        assertEquals(BigDecimal.ZERO, existedCart.getItemsCost());
        long actualUserId = createdOrder.getUser().getId();
        assertEquals(user.getId(), actualUserId);
    }

    private Cart getEmptyUserCart(User user) {
        Cart cart = new Cart(user);
        cart.setId(user.getId());
        cart.setItemsCost(BigDecimal.ZERO);
        return cart;
    }

    private Cart prepareUserCartForOrder(User user) {
        Cart cart = new Cart(user);
        cart.setId(user.getId());
        Set<Tag> tags = Stream.of(
                new Tag(0L, "fitness_and_sports", null),
                new Tag(0L, "travel", null)
        ).collect(Collectors.toSet());
        GiftCertificate certificate = new GiftCertificate(
                0L,
                "Express alloy on kayakes",
                "When you sit in the city, get bored of the routine and the lack of fresh impressions, you should throw something new into your life.",
                BigDecimal.valueOf(160.00),
                ZonedDateTime.now(),
                ZonedDateTime.now().plusDays(1),
                Duration.ofDays(30),
                tags,
                true
        );
        int quantity = 5;
        Set<CartItem> cartItems = Stream.of(
                new CartItem(cart, certificate, quantity)
        ).collect(Collectors.toSet());
        cart.setItems(cartItems);
        return cart;
    }

    private void fillCartItemsCost(Cart cart) {
        BigDecimal cost = cart.getItems().stream()
                .map(this::calculateCartItemCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setItemsCost(cost);
    }

    private BigDecimal calculateCartItemCost(CartItem cartItem) {
        BigDecimal itemsCount = BigDecimal.valueOf(cartItem.getQuantity());
        GiftCertificate certificate = cartItem.getGiftCertificate();
        return certificate.getPrice().multiply(itemsCount);
    }

    private Order createNewOrder(User user, Cart cart) {
        Order order = new Order();

        order.setUser(user);
        order.setPrice(cart.getItemsCost());

        return order;
    }

    private void fillOrderItems(Cart cart, Order order) {
        List<OrderItem> ordered = cart.getItems().stream()
                .map(item -> createOrderedCertificate(order, item))
                .collect(Collectors.toList());
        order.setOrderItems(ordered);
    }

    private OrderItem createOrderedCertificate(Order order, CartItem item) {
        OrderItem orderedCertificate = new OrderItem();

        orderedCertificate.setGiftCertificate(item.getGiftCertificate());
        orderedCertificate.setOrder(order);
        orderedCertificate.setQuantity(item.getQuantity());

        return orderedCertificate;
    }

    private Cart clearCart(Cart cart) {
        cart.getItems().clear();
        cart.setItemsCost(BigDecimal.ZERO);
        return cartRepository.save(cart);
    }
}
