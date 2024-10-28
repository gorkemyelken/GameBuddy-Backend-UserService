package com.gamebuddy.user.dto;

import com.gamebuddy.user.model.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateDTO {

    @Size(min = 3, max = 20, message = "User name must be between 3 and 20 characters")
    private String userName;

    @Email(message = "Email should be valid")
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    private Gender gender;

    private Integer age;

    private String profilePhoto;

    private boolean isPremium;
}
