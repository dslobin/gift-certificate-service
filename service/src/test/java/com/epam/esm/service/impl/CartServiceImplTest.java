package com.epam.esm.service.impl;

import com.epam.esm.dao.CartDao;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.CartDto;
import com.epam.esm.entity.Cart;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.mapper.CartMapper;
import com.epam.esm.service.CartService;
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
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = ServiceContextTest.class)
class CartServiceImplTest {
    @Autowired
    private CartDao cartDao;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private UserDao userDao;
    @Autowired
    private GiftCertificateDao giftCertificateDao;
    @Autowired
    private CartService cartService;

    @Test
    void givenUserEmail_whenGetCartOrCreate_thenGetUserCart() {
        Set<Role> roles = Stream.of(new Role(1L, "USER", null)).collect(Collectors.toSet());
        User user = new User(1L, "jared.mccarthy@mail.com", "123456", "Jared", "Mccarthy", null, roles);
        Cart cart = new Cart(user);

        given(userDao.findByEmail(any(String.class))).willReturn(Optional.of(user));
        given(cartDao.findById(any(Long.class))).willReturn(Optional.of(cart));

        String userEmail = "jared.mccarthy@mail.com";
        CartDto cartDto = cartService.getCartOrCreate(userEmail);
        assertThat(cartDto).isNotNull();
    }

    @Test
    void givenUserEmail_whenGetCartOrCreate_thenCreateUserCart() {
        Set<Role> roles = Stream.of(new Role(1L, "USER", null)).collect(Collectors.toSet());
        User user = new User(1L, "jared.mccarthy@mail.com", "123456", "Jared", "Mccarthy", null, roles);
        Cart cart = new Cart(user);

        given(userDao.findByEmail(any(String.class))).willReturn(Optional.of(user));
        given(cartDao.findById(any(Long.class))).willReturn(Optional.empty());
        given(cartDao.save(any(Cart.class))).willReturn(cart);

        String userEmail = "jared.mccarthy@mail.com";
        CartDto cartDto = cartService.getCartOrCreate(userEmail);
        assertThat(cartDto).isNotNull();
    }

    @Test
    void givenOrderItem_whenAddToCart_thenGetCorrectCartItem() {
        Set<Role> roles = Stream.of(new Role(1L, "USER", null)).collect(Collectors.toSet());
        User user = new User(1L, "jared.mccarthy@mail.com", "123456", "Jared", "Mccarthy", null, roles);
        Cart cart = new Cart(user);
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

        given(userDao.findByEmail(any(String.class))).willReturn(Optional.of(user));
        given(cartDao.findById(any(Long.class))).willReturn(Optional.of(cart));
        given(giftCertificateDao.findById(any(Long.class))).willReturn(Optional.of(certificate));
        given(cartDao.save(cart)).willReturn(cart);

        String userEmail = "jared.mccarthy@mail.com";
        long certificateId = certificate.getId();
        int quantity = 1;
        CartDto cartDto = cartService.addToCart(userEmail, certificateId, quantity);
        assertThat(cartDto).isNotNull();
    }

    @Test
    void givenUserEmail_whenClearCart_thenGetOk() {
        Set<Role> roles = Stream.of(new Role(1L, "USER", null)).collect(Collectors.toSet());
        User user = new User(1L, "jared.mccarthy@mail.com", "123456", "Jared", "Mccarthy", null, roles);
        Cart cart = new Cart(user);

        given(userDao.findByEmail(any(String.class))).willReturn(Optional.of(user));
        given(cartDao.save(cart)).willReturn(cart);

        String userEmail = "jared.mccarthy@mail.com";
        CartDto cartDto = cartService.clearCart(userEmail);
        assertThat(cartDto).isNotNull();
        assertTrue(cart.isEmpty());
    }
}
