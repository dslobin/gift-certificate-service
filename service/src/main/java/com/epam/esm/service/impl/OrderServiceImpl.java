package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.entity.*;
import com.epam.esm.service.CartService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;
    private final UserService userService;
    private final CartService cartService;

    @Override
    public List<Order> findUserOrders(String userEmail) {
        return orderDao.findByUserEmail(userEmail);
    }

    @Override
    public Optional<Order> findUserOrder(String userEmail, long orderId) {
        return orderDao.findByIdAndUserEmail(orderId, userEmail);
    }

    // TODO: заменить IllegalStateException и IllegalArgumentException на свой exception
    @Override
    public Order createUserOrder(String userEmail) {
        User user = userService.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException(userEmail));

        Cart cart = cartService.getCartOrCreate(user.getEmail());
        if (cart.isEmpty()) {
            throw new IllegalStateException();
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
        order.setCreatedAt(ZonedDateTime.now());
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
        OrderItem orderedProduct = new OrderItem();

        orderedProduct.setGiftCertificate(item.getGiftCertificate());
        orderedProduct.setOrder(order);
        orderedProduct.setQuantity(item.getQuantity());

        return orderedProduct;
    }
}
