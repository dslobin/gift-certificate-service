package com.epam.esm.security;

import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UserNotFoundException {
        User user = userService.findByEmail(userEmail);

        boolean enabled = user.isEnabled();
        boolean accountNonExpired = user.isEnabled();
        boolean credentialsNonExpired = user.isEnabled();
        boolean accountNonLocked = user.isEnabled();
        Set<GrantedAuthority> userRoles = getAuthorities(user.getRoles());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), enabled,
                accountNonExpired, credentialsNonExpired, accountNonLocked,
                userRoles
        );
    }

    private Set<GrantedAuthority> getAuthorities(Set<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
    }
}
