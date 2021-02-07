package com.epam.esm.service.impl;

import com.epam.esm.entity.Cart;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.repository.CartRepository;
import com.epam.esm.service.CartService;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.config.ServiceContextTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = ServiceContextTest.class)
class CartServiceImplTest {
    @Autowired
    private CartRepository cartDao;
    @MockBean
    private UserService userService;
    @MockBean
    private GiftCertificateService certificateService;
    @Autowired
    private CartService cartService;

    @Test
    void givenUserEmail_whenGetCartOrCreate_thenGetUserCart() {
        Set<Role> roles = Stream.of(new Role(1L, "USER", null)).collect(Collectors.toSet());
        User user = new User(1L, "jared.mccarthy@mail.com", "123456", "Jared", "Mccarthy", null, roles, true);
        Cart cart = new Cart(user);

        given(userService.findByEmail(any(String.class))).willReturn(user);
        given(cartDao.findById(any(Long.class))).willReturn(Optional.of(cart));

        String userEmail = "jared.mccarthy@mail.com";
        Cart actualCart = cartService.getCartOrCreate(userEmail);
        assertThat(actualCart).isNotNull();
    }

    @Test
    void givenUserEmail_whenGetCartOrCreate_thenCreateUserCart() {
        Set<Role> roles = Stream.of(new Role(1L, "USER", null)).collect(Collectors.toSet());
        User user = new User(1L, "jared.mccarthy@mail.com", "123456", "Jared", "Mccarthy", null, roles, true);
        Cart cart = new Cart(user);

        given(userService.findByEmail(any(String.class))).willReturn(user);
        given(cartDao.findById(any(Long.class))).willReturn(Optional.empty());
        given(cartDao.save(any(Cart.class))).willReturn(cart);

        String userEmail = "jared.mccarthy@mail.com";
        Cart createdCart = cartService.getCartOrCreate(userEmail);
        assertThat(createdCart).isNotNull();
    }

    @Test
    void givenCartItem_whenAddToCart_thenGetCorrectCartItem() {
        Set<Role> roles = Stream.of(new Role(1L, "USER", null)).collect(Collectors.toSet());
        User user = new User(1L, "jared.mccarthy@mail.com", "123456", "Jared", "Mccarthy", null, roles, true);
        Cart cart = new Cart(user);
        ZonedDateTime createDate = ZonedDateTime.now();
        GiftCertificate certificate = new GiftCertificate(
                1L,
                "Culinary master class Italian cuisine",
                "A culinary master class is an opportunity to become a Chef of a real Italian restaurant for 3 hours.",
                BigDecimal.valueOf(95.00),
                createDate,
                createDate.plusDays(3),
                Duration.ofDays(14),
                new HashSet<>(),
                true
        );

        given(userService.findByEmail(any(String.class))).willReturn(user);
        given(cartDao.findById(any(Long.class))).willReturn(Optional.of(cart));
        given(certificateService.findById(any(Long.class))).willReturn(certificate);
        given(cartDao.save(cart)).willReturn(cart);

        String userEmail = "jared.mccarthy@mail.com";
        long certificateId = certificate.getId();
        int quantity = 1;
        Cart actualCart = cartService.addToCart(userEmail, certificateId, quantity);
        assertThat(actualCart).isNotNull();
        int cartItemsCount = 1;
        assertEquals(cartItemsCount, actualCart.getItemsCount());
    }

    @Test
    void givenCartItem_whenRemoveCertificateFromCart_thenGetEmptyCart() {
        Set<Role> roles = Stream.of(new Role(1L, "USER", null)).collect(Collectors.toSet());
        User user = new User(1L, "jared.mccarthy@mail.com", "123456", "Jared", "Mccarthy", null, roles, true);
        Cart cart = new Cart(user);
        ZonedDateTime createDate = ZonedDateTime.now();
        GiftCertificate certificate = new GiftCertificate(
                1L,
                "Culinary master class Italian cuisine",
                "A culinary master class is an opportunity to become a Chef of a real Italian restaurant for 3 hours.",
                BigDecimal.valueOf(95.00),
                createDate,
                createDate.plusDays(3),
                Duration.ofDays(14),
                new HashSet<>(),
                true
        );

        given(userService.findByEmail(any(String.class))).willReturn(user);
        given(cartDao.findById(any(Long.class))).willReturn(Optional.of(cart));
        given(certificateService.findById(any(Long.class))).willReturn(certificate);
        given(cartDao.save(cart)).willReturn(cart);

        String userEmail = "jared.mccarthy@mail.com";
        long certificateId = certificate.getId();
        int quantity = 0;
        Cart actualCart = cartService.addToCart(userEmail, certificateId, quantity);
        assertThat(actualCart).isNotNull();
        int cartItemsCount = 0;
        assertEquals(cartItemsCount, actualCart.getItemsCount());
    }


    @Test
    void givenCartItem_whenCertificatesIsNull_thenCartIsNotUpdated() {
        Set<Role> roles = Stream.of(new Role(1L, "USER", null)).collect(Collectors.toSet());
        User user = new User(1L, "jared.mccarthy@mail.com", "123456", "Jared", "Mccarthy", null, roles, true);
        Cart cart = new Cart(user);

        given(userService.findByEmail(any(String.class))).willReturn(user);
        given(cartDao.findById(any(Long.class))).willReturn(Optional.of(cart));
        given(certificateService.findById(any(Long.class))).willReturn(null);
        given(cartDao.save(cart)).willReturn(cart);

        String userEmail = "jared.mccarthy@mail.com";
        long certificateId = 1000;
        int quantity = 0;
        Cart actualCart = cartService.addToCart(userEmail, certificateId, quantity);

        assertEquals(cart, actualCart);
    }

    @Test
    void givenCartItem_whenAddToCartUnavailableCertificate_thenAddingWontHappen() {
        Set<Role> roles = Stream.of(new Role(1L, "USER", null)).collect(Collectors.toSet());
        User user = new User(1L, "jared.mccarthy@mail.com", "123456", "Jared", "Mccarthy", null, roles, true);
        Cart cart = new Cart(user);
        ZonedDateTime createDate = ZonedDateTime.now();
        GiftCertificate certificate = new GiftCertificate(
                1L,
                "Culinary master class Italian cuisine",
                "A culinary master class is an opportunity to become a Chef of a real Italian restaurant for 3 hours.",
                BigDecimal.valueOf(95.00),
                createDate,
                createDate.plusDays(3),
                Duration.ofDays(14),
                new HashSet<>(),
                false
        );

        given(userService.findByEmail(any(String.class))).willReturn(user);
        given(cartDao.findById(any(Long.class))).willReturn(Optional.of(cart));
        given(certificateService.findById(any(Long.class))).willReturn(certificate);
        given(cartDao.save(cart)).willReturn(cart);

        String userEmail = "jared.mccarthy@mail.com";
        long certificateId = certificate.getId();
        int quantity = 1;
        Cart actualCart = cartService.addToCart(userEmail, certificateId, quantity);
        assertThat(actualCart).isNotNull();
        int cartItemsCount = 0;
        assertEquals(cartItemsCount, actualCart.getItemsCount());
    }

    @Test
    void givenUserEmail_whenClearCart_thenGetOk() {
        Set<Role> roles = Stream.of(new Role(1L, "USER", null)).collect(Collectors.toSet());
        User user = new User(1L, "jared.mccarthy@mail.com", "123456", "Jared", "Mccarthy", null, roles, true);
        Cart cart = new Cart(user);

        given(userService.findByEmail(any(String.class))).willReturn(user);
        given(cartDao.save(cart)).willReturn(cart);

        String userEmail = "jared.mccarthy@mail.com";
        Cart actualCart = cartService.clearCart(userEmail);
        assertThat(actualCart).isNotNull();
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void givenCart_whenCheckIfEmpty_thenGetTrue() {
        Set<Role> roles = Stream.of(new Role(1L, "USER", null)).collect(Collectors.toSet());
        User user = new User(1L, "jared.mccarthy@mail.com", "123456", "Jared", "Mccarthy", null, roles, true);
        Cart cart = new Cart(user);

        boolean isEmpty = cartService.isCartEmpty(cart);
        assertTrue(isEmpty);
    }
}
