package com.gamebuddy.user.service;

import com.gamebuddy.user.dto.ReviewCreateDTO;
import com.gamebuddy.user.dto.ReviewViewDTO;
import com.gamebuddy.user.exception.ReviewNotFoundException;
import com.gamebuddy.user.model.Review;
import com.gamebuddy.user.repository.ReviewRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, ModelMapper modelMapper) {
        this.reviewRepository = reviewRepository;
        this.modelMapper = modelMapper;
    }

    public ReviewViewDTO createReview(ReviewCreateDTO reviewCreateDTO) {
        Review review = modelMapper.map(reviewCreateDTO, Review.class);
        review.setId(UUID.randomUUID().toString());
        review.setCreatedAt(LocalDateTime.now());
        reviewRepository.save(review);
        return modelMapper.map(review, ReviewViewDTO.class);
    }

    public ReviewViewDTO getReviewById(String reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with id: " + reviewId));
        return modelMapper.map(review, ReviewViewDTO.class);
    }

    public void deleteReview(String reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new ReviewNotFoundException("Review not found with id: " + reviewId);
        }
        reviewRepository.deleteById(reviewId);
    }
}
