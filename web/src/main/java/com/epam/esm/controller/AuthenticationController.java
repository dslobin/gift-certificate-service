package com.epam.esm.controller;

import com.epam.esm.dto.AuthResponse;
import com.epam.esm.dto.LoginRequest;
import com.epam.esm.dto.SignUpRequest;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import com.epam.esm.exception.EmailExistException;
import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.mapper.UserMapper;
import com.epam.esm.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final AuthenticationService authService;
    private final UserMapper userMapper;

    /**
     * Authenticates the user.
     *
     * @throws UserNotFoundException   if the user with the specified email doesn't exist
     * @throws BadCredentialsException if the password or login is entered incorrectly
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest)
            throws UserNotFoundException, BadCredentialsException {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        AuthResponse authResponse = authService.authenticate(email, password);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authResponse);
    }

    /**
     * Registers a user.
     *
     * @throws EmailExistException if the user with the specified email already exist
     */
    @PostMapping("/sign-up")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpRequest signUpRequest)
            throws EmailExistException {
        User createdUser = authService.signUp(signUpRequest);
        log.debug("User successfully registered: {}", createdUser);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userMapper.toDto(createdUser));
    }
}
