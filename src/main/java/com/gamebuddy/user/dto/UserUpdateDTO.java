package com.gamebuddy.user.dto;

import com.gamebuddy.user.model.Gender;
import com.gamebuddy.user.model.LanguagePreference;
import lombok.Data;

import java.util.Set;

@Data
public class UserUpdateDTO {

    private String userName;

    private String email;

    private String password;

    private Gender gender;

    private Integer age;

    private String profilePhoto;

    private boolean isPremium;

    private Set<LanguagePreference> preferredLanguages;
}
