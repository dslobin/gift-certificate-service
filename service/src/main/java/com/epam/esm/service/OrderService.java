package com.epam.esm.service;

import com.epam.esm.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> findUserOrders(String userEmail);

    Optional<Order> findUserOrder(String userEmail, long orderId);

    Order createUserOrder(String userEmail);
}
