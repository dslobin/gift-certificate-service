package com.epam.esm.service.impl;

import com.epam.esm.dto.SignUpRequest;
import com.epam.esm.entity.*;
import com.epam.esm.exception.EmailExistException;
import com.epam.esm.exception.RoleNotFoundException;
import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.RoleService;
import com.epam.esm.service.UserService;
import com.epam.esm.util.Translator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final OrderRepository orderRepository;
    private final Translator translator;

    private static final String ROLE_USER = "ROLE_USER";

    @Override
    @Transactional(readOnly = true)
    public Set<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).get()
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(long id)
            throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(String.format(translator.toLocale("error.notFound.userId"), id)));
    }

    @Override
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(String.format(translator.toLocale("error.notFound.userEmail"), email)));
    }

    @Override
    @Transactional
    public User create(SignUpRequest userData)
            throws EmailExistException, RoleNotFoundException {
        String userEmail = userData.getEmail();
        log.debug("Trying to add a user with email: {}", userEmail);
        Optional<User> userOptional = userRepository.findByEmail(userEmail);
        if (userOptional.isPresent()) {
            log.error("A user with this email: {} already exists", userEmail);
            throw new EmailExistException(String.format(translator.toLocale("error.badRequest.emailExist"), userEmail));
        }

        User user = new User();

        user.setEmail(userEmail);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(userData.getPassword());
        user.setPassword(hashedPassword);
        user.setFirstName(userData.getFirstName());
        user.setLastName(userData.getLastName());
        Cart userCart = new Cart(user);
        user.setCart(userCart);
        Role roleUser = roleService.findByName(ROLE_USER)
                .orElseThrow(() -> new RoleNotFoundException(String.format(translator.toLocale("error.notFound.roleName"), ROLE_USER)));
        user.setRoles(Collections.singleton(roleUser));
        user.setEnabled(true);

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public Set<Tag> findMostUsedUserTag(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        List<Order> orders = orderRepository.findByUserEmail(user.getEmail()).stream()
                .sorted(Comparator.comparing(Order::getPrice))
                .collect(Collectors.toList());
        List<GiftCertificate> orderedCertificates = getOrderedCertificates(orders);
        Map<Tag, Long> tagRepetitionNumber = countTagRepetitionNumber(orderedCertificates);
        tagRepetitionNumber.forEach((k, v) -> log.debug("{} : {}", k, v));
        return getMostUsedTagsWithHighestOrderCost(tagRepetitionNumber, orders);
    }

    private Set<Tag> getMostUsedTagsWithHighestOrderCost(
            Map<Tag, Long> tagRepetitionNumber,
            List<Order> userOrders
    ) {
        Long tagUsesMaxNumber = tagRepetitionNumber.values().stream()
                .max(Comparator.naturalOrder())
                .orElse(0L);
        log.debug("Maximum number of tag uses = {}", tagUsesMaxNumber);
        BigDecimal maxOrderPrice = userOrders.stream()
                .map(Order::getPrice)
                .max(Comparator.naturalOrder())
                .orElse(BigDecimal.ZERO);
        log.debug("Maximum order price = {}", maxOrderPrice);
        Set<Tag> mostCommonlyUsedTags = tagRepetitionNumber.entrySet().stream()
                .filter(t -> t.getValue().equals(tagUsesMaxNumber))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        log.debug("The most commonly used tags: {}", mostCommonlyUsedTags);
        List<Order> ordersWithMaxPrice = userOrders.stream()
                .filter(order -> order.getPrice().equals(maxOrderPrice))
                .collect(Collectors.toList());
        log.debug("Most expensive user orders: {}", ordersWithMaxPrice);
        return getOrderedCertificates(ordersWithMaxPrice).stream()
                .map(GiftCertificate::getTags)
                .flatMap(Collection::stream)
                .filter(mostCommonlyUsedTags::contains)
                .collect(Collectors.toSet());
    }

    private List<GiftCertificate> getOrderedCertificates(List<Order> orders) {
        return orders.stream()
                .map(Order::getOrderItems)
                .flatMap(Collection::stream)
                .map(OrderItem::getGiftCertificate)
                .collect(Collectors.toList());
    }

    private Map<Tag, Long> countTagRepetitionNumber(List<GiftCertificate> certificates) {
        List<Tag> allTags = getAllTagsFromOrderedCertificates(certificates);
        HashMap<Tag, Long> tagUsageNumber = new LinkedHashMap<>();
        allTags.forEach(tag -> tagUsageNumber.put(tag, tagUsageNumber.getOrDefault(tag, 0L) + 1));
        return tagUsageNumber.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    private List<Tag> getAllTagsFromOrderedCertificates(List<GiftCertificate> certificates) {
        return certificates.stream()
                .map(GiftCertificate::getTags)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
