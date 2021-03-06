package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserEmail(String userEmail);

    List<Order> findByUserEmail(String userEmail, Pageable pageable);

    Optional<Order> findByIdAndUserEmail(long orderId, String userEmail);
}
