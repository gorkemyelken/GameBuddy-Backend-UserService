package com.gamebuddy.user.dto;

import com.gamebuddy.user.model.Confirmation;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewViewDTO {
    private String reviewId;
    private String reviewerUserId;
    private String reviewedUserId;
    private Float rating;
    private String comment;
    private Confirmation genderConfirmation;
    private Confirmation ageConfirmation;
    private LocalDateTime createdAt;
}
