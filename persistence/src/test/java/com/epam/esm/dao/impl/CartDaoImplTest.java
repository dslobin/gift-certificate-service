package com.epam.esm.dao.impl;

import com.epam.esm.dao.CartDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.config.JpaContextTest;
import com.epam.esm.entity.Cart;
import com.epam.esm.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JpaContextTest.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class CartDaoImplTest {
    @Autowired
    private CartDao cartDao;
    @Autowired
    private UserDao userDao;

    @Test
    @Sql(scripts = {"/test-users-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenMultipleCarts_whenFindById_thenGetCorrectCart() {
        long requiredCartId = 5;
        Optional<Cart> actualCart = cartDao.findById(requiredCartId);

        assertTrue(actualCart.isPresent());

        long actualCartId = actualCart.get().getId();
        assertEquals(requiredCartId, actualCartId);
    }

    @Test
    @Sql(scripts = {"/test-one-user-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenCart_whenSave_thenGetCorrectUserId() {
        String userEmail = "jared.mccarthy@mail.com";

        Optional<User> userOptional = userDao.findByEmail(userEmail);

        assertTrue(userOptional.isPresent());

        User user = userOptional.get();
        Cart cart = new Cart(user);
        Cart savedCart = cartDao.save(cart);

        long requiredUserId = savedCart.getUser().getId();
        long actualUserId = user.getId();
        assertEquals(requiredUserId, actualUserId);
    }

    @Test
    @Sql(scripts = {"/test-users-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenMultipleCarts_whenFindByUserEmail_thenGetCorrectCart() {
        String userEmail = "felix.ryan@mail.com";
        Cart userCart = cartDao.findByUserEmail(userEmail);

        String userEmailFromCart = userCart.getUser().getEmail();
        assertEquals(userEmail, userEmailFromCart);
    }
}
