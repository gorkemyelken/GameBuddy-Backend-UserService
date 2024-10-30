package com.gamebuddy.user.repository;

import com.gamebuddy.user.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, String> {

    Review findByReviewId(String reviewId);

    boolean existsByReviewId(String reviewId);

    Review findByReviewedUserId(String reviewedUserId);

    boolean existsByReviewedUserId(String reviewedUserId);
}
