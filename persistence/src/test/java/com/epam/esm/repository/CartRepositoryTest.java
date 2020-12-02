package com.epam.esm.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JpaContextTest.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional*/
class CartDaoImplTest {
/*    @Autowired
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
    }*/
}
