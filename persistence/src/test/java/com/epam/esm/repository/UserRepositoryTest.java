package com.epam.esm.repository;

import com.epam.esm.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserDaoImplTest {
    @Autowired
    private UserRepository userDao;

    @Test
    @Sql(scripts = {"/test-users-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenMultipleUsers_whenFindAll_thenGetCorrectCount() {
        List<User> actualUsers = userDao.findAll();

        int expectedUsersCount = 5;
        assertEquals(expectedUsersCount, actualUsers.size());
    }

    @Test
    @Sql(scripts = {"/test-users-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenMultipleUsers_whenFindById_thenGetCorrectUser() {
        long requiredUserId = 2;
        Optional<User> actualUser = userDao.findById(requiredUserId);

        assertTrue(actualUser.isPresent());

        long actualUserId = actualUser.get().getId();
        assertEquals(requiredUserId, actualUserId);
    }

    @Test
    @Sql(scripts = {"/test-users-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenMultipleUsers_whenFindByEmail_thenGetCorrectUser() {
        String requiredUserEmail = "hanna.robbins@mail.com";
        Optional<User> actualUser = userDao.findByEmail(requiredUserEmail);

        assertTrue(actualUser.isPresent());

        String actualUserEmail = actualUser.get().getEmail();
        assertEquals(requiredUserEmail, actualUserEmail);
    }
}
