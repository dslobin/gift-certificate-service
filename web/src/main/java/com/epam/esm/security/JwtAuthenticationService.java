package com.epam.esm.security;

import com.epam.esm.dto.AuthResponse;
import com.epam.esm.dto.SignUpRequest;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.exception.EmailExistException;
import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.mapper.RoleMapper;
import com.epam.esm.security.jwt.JwtTokenProvider;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationService implements AuthenticationService {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RoleMapper roleMapper;

    @Override
    public AuthResponse authenticate(String email, String password)
            throws UserNotFoundException, BadCredentialsException {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
            authenticationManager.authenticate(authenticationToken);
            User user = userService.findByEmail(email);
            String userEmail = user.getEmail();
            Set<Role> userRoles = user.getRoles();
            String jwtToken = jwtTokenProvider.createToken(userEmail, userRoles);
            return new AuthResponse(userEmail, jwtToken, roleMapper.toDto(userRoles));
        } catch (AuthenticationException e) {
            log.error("The user: {} entered an incorrect username or password", email);
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @Override
    public User signUp(SignUpRequest userData)
            throws EmailExistException {
        return userService.create(userData);
    }
}
