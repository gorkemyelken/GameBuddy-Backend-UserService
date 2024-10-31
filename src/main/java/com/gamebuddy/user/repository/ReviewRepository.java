package com.gamebuddy.user.repository;

import com.gamebuddy.user.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {

    Review findByReviewId(String reviewId);

    boolean existsByReviewId(String reviewId);

    List<Review> findReviewsByReviewedUserId(String reviewedUserId);

    boolean existsByReviewedUserId(String reviewedUserId);
}
