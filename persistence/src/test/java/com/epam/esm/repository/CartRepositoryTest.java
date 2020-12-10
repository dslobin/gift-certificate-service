package com.epam.esm.repository;

import com.epam.esm.entity.Cart;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
class CartRepositoryTest {
    @Autowired
    private CartRepository cartRepository;
    @MockBean
    private UserRepository userRepository;

    @Test
    @Sql(scripts = {"/test-users-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenMultipleCarts_whenFindById_thenGetCorrectCart() {
        long requiredCartId = 5;
        Optional<Cart> actualCart = cartRepository.findById(requiredCartId);

        assertTrue(actualCart.isPresent());

        long actualCartId = actualCart.get().getId();
        assertEquals(requiredCartId, actualCartId);
    }

    @Test
    void givenCart_whenSave_thenGetCorrectUserId() {
        Set<Role> roles = Stream.of(new Role(1L, "ROLE_USER", null)).collect(Collectors.toSet());
        String userEmail = "jared.mccarthy@mail.com";
        User user = new User(1L, userEmail, "123456", "Jared", "Mccarthy", null, roles, true);

        given(userRepository.findByEmail(userEmail)).willReturn(Optional.of(user));

        User savedUser = userRepository.findByEmail(userEmail).orElse(null);

        assertTrue(Objects.nonNull(savedUser));

        Cart cart = new Cart(savedUser);
        Cart savedCart = cartRepository.save(cart);

        long requiredUserId = savedCart.getUser().getId();
        long actualUserId = savedUser.getId();
        assertEquals(requiredUserId, actualUserId);
    }

    @Test
    @Sql(scripts = {"/test-users-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenMultipleCarts_whenFindByUserEmail_thenGetCorrectCart() {
        String userEmail = "felix.ryan@mail.com";
        Cart userCart = cartRepository.findByUserEmail(userEmail).orElse(null);

        assertTrue(Objects.nonNull(userCart));
        String userEmailFromCart = userCart.getUser().getEmail();
        assertEquals(userEmail, userEmailFromCart);
    }
}
