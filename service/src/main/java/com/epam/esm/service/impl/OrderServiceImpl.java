package com.epam.esm.service.impl;

import com.epam.esm.entity.*;
import com.epam.esm.exception.EmptyCartException;
import com.epam.esm.exception.OrderNotFoundException;
import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.service.CartService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderDao;
    private final UserService userService;
    private final CartService cartService;

    private static final String ORDER_NOT_FOUND = "error.notFound.order";
    private static final String EMPTY_CART = "error.notFound.order";

    @Override
    @Transactional(readOnly = true)
    public List<Order> findUserOrders(String userEmail, Pageable pageable)
            throws UserNotFoundException {
        User user = userService.findByEmail(userEmail);
        return orderDao.findByUserEmail(user.getEmail(), pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findUserOrders(String userEmail)
            throws UserNotFoundException {
        User user = userService.findByEmail(userEmail);
        return orderDao.findByUserEmail(user.getEmail());
    }

    @Override
    @Transactional(readOnly = true)
    public Order findUserOrder(String userEmail, long orderId)
            throws OrderNotFoundException {
        return orderDao.findByIdAndUserEmail(orderId, userEmail)
                .orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND, orderId, userEmail));
    }

    @Override
    @Transactional
    public Order createUserOrder(String userEmail)
            throws UserNotFoundException, EmptyCartException {
        User user = userService.findByEmail(userEmail);

        Cart cart = cartService.getCartOrCreate(user.getEmail());
        if (cartService.isCartEmpty(cart)) {
            log.error("The user with email = {} tried to create an order with an empty cart", userEmail);
            throw new EmptyCartException(EMPTY_CART);
        }

        Order order = createNewOrder(user, cart);

        fillOrderItems(cart, order);
        orderDao.save(order);

        cartService.clearCart(userEmail);
        return order;
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
}
