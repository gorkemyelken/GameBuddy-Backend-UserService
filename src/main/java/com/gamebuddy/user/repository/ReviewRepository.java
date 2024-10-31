package com.gamebuddy.user.repository;

import com.gamebuddy.user.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, String> {

    Review findByReviewId(String reviewId);

    boolean existsByReviewId(String reviewId);

    List<Review> findReviewsByReviewedUserId(String reviewedUserId);

    boolean existsByReviewedUserId(String reviewedUserId);
}
