package com.gamebuddy.user.dto;

import com.gamebuddy.user.model.Gender;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserViewDTO {
    private String id;
    private String userName;
    private String email;
    private Gender gender;
    private Integer age;
    private String profilePhoto;
    private boolean isPremium;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
