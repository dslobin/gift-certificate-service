package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderItem;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.config.ServiceContextTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = ServiceContextTest.class)
class UserServiceImplTest {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderDao orderDao;

    @Test
    void givenUsers_whenFindAll_thenGetCorrectUserSize() {
        Set<Role> roles = Stream.of(
                new Role(1L, "USER", null)
        ).collect(Collectors.toSet());
        List<User> tags = Stream.of(
                new User(1L, "jared.mccarthy@mail.com", "123456", "Jared", "Mccarthy", null, roles),
                new User(2L, "hanna.robbins@mail.com", "123456", "Hanna", "Robbins", null, roles),
                new User(3L, "emilia.malone@mail.com", "123456", "Emilia", "Malone", null, roles)
        ).collect(Collectors.toList());

        int page = 1;
        int size = 5;
        doReturn(tags).when(userDao).findAll(page, size);

        List<UserDto> actualUsers = userService.findAll(page, size);
        int expectedUsersSize = tags.size();
        int actualUsersSize = actualUsers.size();
        assertEquals(expectedUsersSize, actualUsersSize);
    }

    @Test
    void givenUser_whenFindById_thenGetCorrectUser() {
        Set<Role> roles = Stream.of(
                new Role(1L, "USER", null)
        ).collect(Collectors.toSet());
        User user = new User(1L, "jared.mccarthy@mail.com", "123456", "Jared", "Mccarthy", null, roles);

        long tagId = 1L;
        given(userDao.findById(tagId)).willReturn(Optional.of(user));

        UserDto actualUser = userService.findById(tagId);

        assertThat(actualUser).isNotNull();
        assertEquals(user.getId(), actualUser.getId());
    }

    @Test
    void givenUser_whenFindByEmail_thenGetCorrectUser() {
        Set<Role> roles = Stream.of(
                new Role(1L, "USER", null)
        ).collect(Collectors.toSet());
        User user = new User(1L, "jared.mccarthy@mail.com", "123456", "Jared", "Mccarthy", null, roles);

        String email = "jared.mccarthy@mail.com";
        given(userDao.findByEmail(email)).willReturn(Optional.of(user));

        UserDto actualUser = userService.findByEmail(email);

        assertThat(actualUser).isNotNull();
        assertEquals(user.getEmail(), actualUser.getEmail());
    }

    @Test
    void givenUserEmail_whenFindFindMostUsedUserTag_thenGetEmptySet() {
        Set<Role> roles = Stream.of(
                new Role(1L, "USER", null)
        ).collect(Collectors.toSet());
        User user = new User(1L, "jared.mccarthy@mail.com", "123456", "Jared", "Mccarthy", null, roles);

        String userEmail = "jared.mccarthy@mail.com";
        given(userDao.findByEmail(userEmail)).willReturn(Optional.of(user));

        List<OrderItem> orderItems = new ArrayList<>();
        ZonedDateTime createDate = ZonedDateTime.now();
        List<Order> orders = Stream.of(
                new Order(1L, BigDecimal.TEN, orderItems, createDate, createDate, user)
        ).collect(Collectors.toList());

        doReturn(orders).when(orderDao).findByUserEmail(userEmail);

        Set<TagDto> tagDtos = userService.findMostUsedUserTag(userEmail);

        assertTrue(tagDtos.isEmpty());
        int expectedTagsCount = 0;
        assertEquals(expectedTagsCount, tagDtos.size());
    }
}
