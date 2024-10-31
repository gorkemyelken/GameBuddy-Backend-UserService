package com.gamebuddy.user.dto;

import com.gamebuddy.user.model.Confirmation;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ReviewViewDTO {
    private String reviewId;
    private String reviewerUserId;
    private String reviewedUserId;
    private int rating;
    private String comment;
    private Confirmation genderConfirmation;
    private Confirmation ageConfirmation;
    private LocalDateTime createdAt;
}
