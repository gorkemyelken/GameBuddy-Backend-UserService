package com.gamebuddy.user.service;

import com.gamebuddy.user.dto.ReviewCreateDTO;
import com.gamebuddy.user.dto.ReviewViewDTO;
import com.gamebuddy.user.exception.results.*;
import com.gamebuddy.user.model.Review;
import com.gamebuddy.user.repository.ReviewRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ReviewService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewService.class);
    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, ModelMapper modelMapper) {
        this.reviewRepository = reviewRepository;
        this.modelMapper = modelMapper;
    }

    public DataResult<ReviewViewDTO> createReview(ReviewCreateDTO reviewCreateDTO) {
        LOGGER.info("[createReview] ReviewCreateDTO: {}",reviewCreateDTO);
        Review review = modelMapper.map(reviewCreateDTO, Review.class);
        review.setReviewId(UUID.randomUUID().toString());
        review.setCreatedAt(LocalDateTime.now());
        reviewRepository.save(review);
        LOGGER.info("[createReview] Review added to database. Review: {}", review);
        ReviewViewDTO reviewViewDTO = modelMapper.map(review, ReviewViewDTO.class);
        return new SuccessDataResult<>(reviewViewDTO, "Review created successfully.");
    }

    public DataResult<ReviewViewDTO> getReviewByReviewId(String reviewId) {
        LOGGER.info("[getReviewByReviewId] ReviewId: {}",reviewId);
        if(!checkIfReviewIdExists(reviewId)){
            return new ErrorDataResult<>("Review not found.");
        }
        Review review = reviewRepository.findByReviewId(reviewId);
        ReviewViewDTO reviewViewDTO = modelMapper.map(review, ReviewViewDTO.class);
        return new SuccessDataResult<>(reviewViewDTO, "Review found successfully.");
    }

    public Result deleteReview(String reviewId) {
        LOGGER.info("[deleteReview] ReviewId: {}",reviewId);
        if (!checkIfReviewIdExists(reviewId)) {
            return new ErrorResult("Review not found with id: " + reviewId);
        }
        reviewRepository.deleteById(reviewId);
        return new SuccessResult("Review deleted successfully.");
    }

    public DataResult<ReviewViewDTO> getReviewByReviewedUserId(String reviewedUserId){
        LOGGER.info("[getReviewByReviewedUserId] ReviewedUserId: {}",reviewedUserId);
        if(!checkIfReviewedUserIdExists(reviewedUserId)){
            return new ErrorDataResult<>("ReviewedUser not found.");
        }
        Review review = reviewRepository.findByReviewedUserId(reviewedUserId);
        ReviewViewDTO reviewViewDTO = modelMapper.map(review, ReviewViewDTO.class);
        return new SuccessDataResult<>(reviewViewDTO, "Review found successfully.");
    }

    private boolean checkIfReviewedUserIdExists(String reviewedUserId) {
        return this.reviewRepository.existsByReviewedUserId(reviewedUserId);
    }

    private boolean checkIfReviewIdExists(String reviewId) {
        return this.reviewRepository.existsByReviewId(reviewId);
    }
}
