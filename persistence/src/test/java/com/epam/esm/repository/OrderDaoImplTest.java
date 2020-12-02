package com.epam.esm.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


// TODO: 27.11.2020
/*@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JpaContextTest.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional*/
class OrderDaoImplTest {
/*    @Autowired
    private OrderDao orderDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private CartDao cartDao;

    @Test
    @Sql(scripts = {"/test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenMultipleOrders_whenFindByUserEmail_thenGetCorrectUserOrders() {
        int page = 1;
        int size = 5;
        String userEmail = "jared.mccarthy.admin@mail.com";

        List<Order> userOrders = orderDao.findByUserEmail(page, size, userEmail);

        int expectedOrdersCount = 2;
        assertEquals(expectedOrdersCount, userOrders.size());
    }

    @Test
    @Sql(scripts = {"/test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenMultipleOrders_whenFindByUserEmailAndOrderId_thenGetCorrectUserOrder() {
        String userEmail = "jared.mccarthy.admin@mail.com";
        long orderId = 1;

        Optional<Order> userOrder = orderDao.findByIdAndUserEmail(orderId, userEmail);

        assertTrue(userOrder.isPresent());

        Order savedOrder = userOrder.get();
        String actualUserEmail = savedOrder.getUser().getEmail();
        assertEquals(userEmail, actualUserEmail);
    }

    @Test
    @Sql(scripts = {"/test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenOrder_whenSave_thenGetCorrectUserId() {
        long userId = 1;

        Optional<User> userOptional = userDao.findById(userId);
        assertTrue(userOptional.isPresent());


        User user = userOptional.get();
        Cart cart = cartDao.findByUserEmail(user.getEmail());
        Order order = createNewOrder(user, cart);
        fillOrderItems(cart, order);
        Order createdOrder = orderDao.save(order);
        cart = clearCart(cart);

        assertTrue(cart.isEmpty());
        long actualUserId = createdOrder.getUser().getId();
        assertEquals(userId, actualUserId);
    }

    private Order createNewOrder(User user, Cart cart) {
        Order order = new Order();

        order.setUser(user);
        ZonedDateTime createDate = ZonedDateTime.now();
        order.setCreatedAt(createDate);
        order.setUpdatedAt(createDate);
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
        cart.clear();
        return cartDao.save(cart);
    }*/
}
