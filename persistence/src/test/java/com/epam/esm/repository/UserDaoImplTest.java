package com.epam.esm.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JpaContextTest.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional*/
class UserDaoImplTest {
/*    @Autowired
    private UserDao userDao;

    @Test
    @Sql(scripts = {"/test-users-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenMultipleUsers_whenFindAll_thenGetCorrectCount() {
        int page = 1;
        int size = 5;
        List<User> actualUsers = userDao.findAll(page, size);

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
    }*/
}
