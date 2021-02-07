package com.epam.esm.security;

import com.epam.esm.dto.AuthResponse;
import com.epam.esm.dto.SignUpRequest;
import com.epam.esm.entity.Cart;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.security.jwt.JwtTokenProvider;
import com.epam.esm.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest
class JwtAuthenticationServiceTest {
    @MockBean
    private UserService userService;
    @MockBean
    private JwtTokenProvider tokenProvider;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private Authentication authentication;
    @Autowired
    private AuthenticationService authenticationService;

    @Test
    void givenEmailAndPassword_whenLogin_thenGetAuthenticatedUser() {
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

        given(authenticationManager.authenticate(any(Authentication.class))).willReturn(authentication);

        given(userService.findByEmail(email)).willReturn(user);

        String jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBtYWlsLmNvbSIsInJvbGVzIjpbIlJPTEVfQURNSU4iXSwiaWF0IjoxNjEyNjkyNzQzLCJleHAiOjE2MTI2OTk5NDN9.kf2SReEqwCs-Vk3XRwmBCtDKQB3wMFdmGo6x2NiCGi4";
        given(tokenProvider.createToken(user.getEmail(), user.getRoles())).willReturn(jwtToken);

        AuthResponse authResponse = authenticationService.authenticate(email, password);

        assertThat(authResponse).isNotNull();
        assertEquals(user.getEmail(), authResponse.getLogin());
        assertEquals(jwtToken, authResponse.getToken());
    }

    @Test
    void givenEmailAndPassword_whenLogin_thenThroeBadCredentialsException() {
        String email = "jared.mccarthy@mail.com";
        String password = "123456";

        given(authenticationManager.authenticate(any(Authentication.class)))
                .willThrow(new BadCredentialsException(""));

        assertThrows(BadCredentialsException.class, () -> {
            authenticationService.authenticate(email, password);
        });
    }

    @Test
    void givenUserData_whenSignUp_thenGetCorrectUser() {
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

        given(userService.create(any(SignUpRequest.class))).willReturn(user);

        SignUpRequest request = new SignUpRequest(email, password, firstName, lastName);
        User actualUser = authenticationService.signUp(request);

        assertThat(actualUser).isNotNull();
        assertEquals(user.getEmail(), actualUser.getEmail());
    }
}
