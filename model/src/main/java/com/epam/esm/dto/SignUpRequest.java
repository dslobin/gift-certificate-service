package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SignUpRequest {
    @Email
    private String email;

    @Min(6)
    private String password;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;
}
