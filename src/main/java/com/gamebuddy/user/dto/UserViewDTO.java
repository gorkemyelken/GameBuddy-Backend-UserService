package com.gamebuddy.user.dto;

import com.gamebuddy.user.model.Gender;
import com.gamebuddy.user.model.LanguagePreference;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UserViewDTO {
    private String userId;
    private String userName;
    private String email;
    private Gender gender;
    private Integer age;
    private String profilePhoto;
    private boolean isPremium;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<LanguagePreference> preferredLanguages;
}
