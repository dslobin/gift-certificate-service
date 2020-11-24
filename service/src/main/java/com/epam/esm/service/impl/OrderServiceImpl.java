package com.epam.esm.service.impl;

import com.epam.esm.dao.CartDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.*;
import com.epam.esm.exception.EmptyCartException;
import com.epam.esm.exception.OrderNotFoundException;
import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.mapper.OrderMapper;
import com.epam.esm.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;
    private final OrderMapper orderMapper;
    private final UserDao userDao;
    private final CartDao cartDao;

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> findUserOrders(
            int page,
            int size,
            String userEmail
    ) {
        User user = userDao.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException(userEmail));
        return orderDao.findByUserEmail(page, size, user.getEmail()).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto findUserOrder(String userEmail, long orderId)
            throws OrderNotFoundException {
        Order userOrder = orderDao.findByIdAndUserEmail(orderId, userEmail)
                .orElseThrow(() -> new OrderNotFoundException(orderId, userEmail));
        return orderMapper.toDto(userOrder);
    }

    @Override
    @Transactional
    public OrderDto createUserOrder(String userEmail)
            throws UserNotFoundException, EmptyCartException {
        User user = userDao.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException(userEmail));

        Cart cart = cartDao.findByUserEmail(user.getEmail());
        if (cart.isEmpty()) {
            throw new EmptyCartException();
        }

        Order order = createNewOrder(user, cart);

        fillOrderItems(cart, order);
        Order createdOrder = orderDao.save(order);

        clearCart(cart);
        return orderMapper.toDto(createdOrder);
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

    private void clearCart(Cart cart) {
        cart.clear();
        cartDao.save(cart);
    }
}
