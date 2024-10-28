package com.gamebuddy.user.repository;

import com.gamebuddy.user.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, String> {
}
