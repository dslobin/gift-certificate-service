package com.epam.esm.security;

import com.epam.esm.dto.AuthResponse;

public interface AuthenticationService {
    AuthResponse authenticate(String email, String password);
}
