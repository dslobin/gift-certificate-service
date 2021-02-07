package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginRequest {
    @Email
    private String email;

    @Min(6)
    private String password;
}
