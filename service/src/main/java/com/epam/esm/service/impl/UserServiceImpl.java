package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.*;
import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.mapper.TagMapper;
import com.epam.esm.mapper.UserMapper;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final UserMapper userMapper;
    private final OrderDao orderDao;
    private final TagMapper tagMapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAll(int page, int size) {
        return userDao.findAll(page, size).stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findById(long id)
            throws UserNotFoundException {
        User user = userDao.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findByEmail(String email) {
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public Set<TagDto> findMostUsedUserTag(String email) {
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        List<Order> orders = orderDao.findByUserEmail(user.getEmail()).stream()
                .sorted(Comparator.comparing(Order::getPrice))
                .collect(Collectors.toList());
        List<GiftCertificate> orderedCertificates = getOrderedCertificates(orders);
        Map<Tag, Long> tagRepetitionNumber = countTagRepetitionNumber(orderedCertificates);
        tagRepetitionNumber.forEach((k, v) -> log.debug("{} : {}", k, v));
        Set<Tag> tags = getMostUsedTagsWithHighestOrderCost(tagRepetitionNumber, orders);
        return tags.stream()
                .map(tagMapper::toDto)
                .collect(Collectors.toSet());
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
