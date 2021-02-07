package com.epam.esm.service.impl;

import com.epam.esm.dto.SignUpRequest;
import com.epam.esm.entity.*;
import com.epam.esm.exception.EmailExistException;
import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.RoleService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.config.ServiceContextTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = ServiceContextTest.class)
class UserServiceImplTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @MockBean
    private RoleService roleService;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    void givenUsers_whenFindAll_thenGetCorrectUserSize() {
        Set<Role> roles = Stream.of(
                new Role(1L, "USER", null)
        ).collect(Collectors.toSet());
        List<User> users = Stream.of(
                new User(1L, "jared.mccarthy@mail.com", "123456", "Jared", "Mccarthy", null, roles, true),
                new User(2L, "hanna.robbins@mail.com", "123456", "Hanna", "Robbins", null, roles, true),
                new User(3L, "emilia.malone@mail.com", "123456", "Emilia", "Malone", null, roles, true)
        ).collect(Collectors.toList());

        int page = 0;
        int size = Integer.MAX_VALUE;
        PageRequest pageable = PageRequest.of(page, size);
        Page<User> userPage = new PageImpl<>(users, pageable, users.size());
        doReturn(userPage).when(userRepository).findAll(pageable);

        Set<User> actualUsers = userService.findAll(pageable);
        int expectedUsersSize = users.size();
        int actualUsersSize = actualUsers.size();
        assertEquals(expectedUsersSize, actualUsersSize);
    }

    @Test
    void givenUser_whenFindById_thenGetCorrectUser() {
        Set<Role> roles = Stream.of(
                new Role(1L, "USER", null)
        ).collect(Collectors.toSet());
        User user = new User(1L, "jared.mccarthy@mail.com", "123456", "Jared", "Mccarthy", null, roles, true);

        long tagId = 1L;
        given(userRepository.findById(tagId)).willReturn(Optional.of(user));

        User actualUser = userService.findById(tagId);

        assertThat(actualUser).isNotNull();
        assertEquals(user.getId(), actualUser.getId());
    }

    @Test
    void givenUserId_whenFindById_thenThrowUserNotFoundException() {
        long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.findById(userId);
        });
    }

    @Test
    void givenUser_whenFindByEmail_thenGetCorrectUser() {
        Set<Role> roles = Stream.of(
                new Role(1L, "USER", null)
        ).collect(Collectors.toSet());
        User user = new User(1L, "jared.mccarthy@mail.com", "123456", "Jared", "Mccarthy", null, roles, true);

        String email = "jared.mccarthy@mail.com";
        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));

        User actualUser = userService.findByEmail(email);

        assertThat(actualUser).isNotNull();
        assertEquals(user.getEmail(), actualUser.getEmail());
    }

    @Test
    void givenUserEmail_whenFindByEmail_thenThrowUserNotFoundException() {
        String userEmail = "jared.mccarthy@mail.com";
        given(userRepository.findByEmail(userEmail)).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.findByEmail(userEmail);
        });
    }

    @Test
    void givenUserData_whenCreate_thenGetCorrectUser() {
        String roleUserName = "ROLE_USER";
        Role roleUser = new Role(1L, roleUserName, null);
        Set<Role> roles = Stream.of(roleUser).collect(Collectors.toSet());
        String email = "jared.mccarthy@mail.com";
        String password = "123456";
        String firstName = "Jared";
        String lastName = "Mccarthy";
        User user = new User(0L, email, password, firstName, lastName, null, roles, true);
        Cart cart = new Cart(user);
        user.setCart(cart);

        given(userRepository.findByEmail(email)).willReturn(Optional.empty());
        given(roleService.findByName(roleUserName)).willReturn(Optional.of(roleUser));
        given(userRepository.save(any(User.class))).willReturn(user);

        SignUpRequest request = new SignUpRequest(email, password, firstName, lastName);
        User actualUser = userService.create(request);

        assertThat(actualUser).isNotNull();
        assertEquals(user.getEmail(), actualUser.getEmail());
    }

    @Test
    void givenUserData_whenCreate_thenGetEmailExistException() {
        String roleUserName = "ROLE_USER";
        Role roleUser = new Role(1L, roleUserName, null);
        Set<Role> roles = Stream.of(roleUser).collect(Collectors.toSet());
        String email = "jared.mccarthy@mail.com";
        String password = "123456";
        String firstName = "Jared";
        String lastName = "Mccarthy";
        User user = new User(0L, email, password, firstName, lastName, null, roles, true);
        Cart cart = new Cart(user);
        user.setCart(cart);

        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));

        SignUpRequest request = new SignUpRequest(email, password, firstName, lastName);
        assertThrows(EmailExistException.class, () -> {
            userService.create(request);
        });
    }

    @Test
    void givenUserEmail_whenFindFindMostUsedUserTag_thenGetEmptySet() {
        Set<Role> roles = Stream.of(
                new Role(1L, "USER", null)
        ).collect(Collectors.toSet());
        String userEmail = "jared.mccarthy@mail.com";
        User user = new User(1L, userEmail, "123456", "Jared", "Mccarthy", null, roles, true);

        given(userRepository.findByEmail(userEmail)).willReturn(Optional.of(user));

        List<OrderItem> orderItems = new ArrayList<>();
        ZonedDateTime createDate = ZonedDateTime.now();
        List<Order> orders = Stream.of(
                new Order(1L, BigDecimal.TEN, orderItems, createDate, createDate, user)
        ).collect(Collectors.toList());

        doReturn(orders).when(orderRepository).findByUserEmail(userEmail);

        Set<Tag> tags = userService.findMostUsedUserTag(userEmail);

        assertTrue(tags.isEmpty());
        int expectedTagsCount = 0;
        assertEquals(expectedTagsCount, tags.size());
    }
}
