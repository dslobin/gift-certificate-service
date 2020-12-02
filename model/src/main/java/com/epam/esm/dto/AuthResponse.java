package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuthResponse {
    private String login;
    private String token;
    private Set<RoleDto> roles = new HashSet<>();
}
