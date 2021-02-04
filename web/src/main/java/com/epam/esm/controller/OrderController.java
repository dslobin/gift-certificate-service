package com.epam.esm.controller;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;
import com.epam.esm.exception.EmptyCartException;
import com.epam.esm.exception.OrderNotFoundException;
import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.mapper.OrderMapper;
import com.epam.esm.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @Value("${pagination.defaultPageValue}")
    private Integer defaultPage;
    @Value("${pagination.maxElementsOnPage}")
    private Integer maxElementsOnPage;

    /**
     * View orders.
     *
     * @return orders list of the specified user
     * @throws UserNotFoundException if the user with the specified email doesn't exist
     */
    @GetMapping
    public ResponseEntity<List<OrderDto>> getOrders(
            @RequestParam(required = false, defaultValue = "${pagination.defaultPageValue}") Integer page,
            @Min(5) @Max(100) @RequestParam(required = false, defaultValue = "${pagination.maxElementsOnPage}") Integer size,
            Principal principal
    ) throws UserNotFoundException {
        String userEmail = principal.getName();
        Pageable pageable = PageRequest.of(page, size);
        List<OrderDto> orders = orderService.findUserOrders(userEmail, pageable).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
        orders.forEach(order -> order.add(linkTo(methodOn(OrderController.class).getOrder(order.getOrderId(), principal)).withSelfRel()));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orders);
    }

    /**
     * Viewing a single order.
     *
     * @return order of the specified user
     * @throws OrderNotFoundException if the order with the specified id nad user email doesn't exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrder(
            @Min(1) @PathVariable Long id,
            Principal principal
    ) {
        String userEmail = principal.getName();
        Order order = orderService.findUserOrder(userEmail, id);
        OrderDto orderDto = orderMapper.toDto(order);
        orderDto.add(
                linkTo(methodOn(OrderController.class).getOrders(defaultPage, maxElementsOnPage, principal)).withRel("userOrders"),
                linkTo(methodOn(OrderController.class).getOrder(id, principal)).withSelfRel()
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderDto);
    }

    /**
     * Order creation.
     *
     * @return created order
     * @throws UserNotFoundException if the user with the specified email doesn't exist
     * @throws EmptyCartException    if the cart is empty
     */
    @PostMapping("/payment")
    public ResponseEntity<OrderDto> createOrder(
            Principal principal
    ) throws EmptyCartException {
        String userEmail = principal.getName();
        Order order = orderService.createUserOrder(userEmail);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderMapper.toDto(order));
    }
}
