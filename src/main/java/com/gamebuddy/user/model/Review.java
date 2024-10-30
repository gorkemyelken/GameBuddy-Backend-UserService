package com.gamebuddy.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    @Id
    @Column(updatable = false, nullable = false)
    private String reviewId;

    private String reviewerUserId;

    private String reviewedUserId;

    private int rating;

    private String comment;

    private LocalDateTime createdAt;
}
