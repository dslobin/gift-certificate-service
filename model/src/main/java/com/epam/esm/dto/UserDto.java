package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {
    @PositiveOrZero
    private long id;

    @Email
    private String email;

    @Size(min = 6)
    private String password;

    private Set<RoleDto> roles = new HashSet<>();
}
