package com.epam.esm.dao;

import com.epam.esm.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderDao {
    List<Order> findByUserEmail(String email);

    Optional<Order> findByIdAndUserEmail(long orderId, String email);

    void save(Order order);
}
