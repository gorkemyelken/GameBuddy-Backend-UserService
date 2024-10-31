package com.gamebuddy.user.dto;

import com.gamebuddy.user.model.Confirmation;
import lombok.Data;

@Data
public class ReviewCreateDTO {

    private String reviewerUserId;

    private String reviewedUserId;

    private Float rating;

    private String comment;

    private Confirmation genderConfirmation;

    private Confirmation ageConfirmation;
}