package com.gamebuddy.user.dto;

import com.gamebuddy.user.model.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCreateDTO {

    @NotBlank(message = "User name is required")
    @Size(min = 3, max = 20, message = "User name must be between 3 and 20 characters")
    private String userName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 20, message = "Password must be at least 8 characters")
    private String password;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotNull(message = "Age is required")
    private Integer age;
}
