package com.gamebuddy.user.dto;

import lombok.Data;

@Data
public class UserCreateDTO {

    private String userName;

    private String email;

    private String password;
}
