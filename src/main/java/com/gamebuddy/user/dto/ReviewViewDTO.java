package com.gamebuddy.user.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ReviewViewDTO {
    private String id;
    private String reviewerUserId;
    private String reviewedUserId;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
}
