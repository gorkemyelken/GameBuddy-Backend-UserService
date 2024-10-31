package com.gamebuddy.user.dto;

import com.gamebuddy.user.model.User;
import lombok.Data;

@Data
public class FriendShipViewDTO {
    private String friendId;

    private User user;
}
