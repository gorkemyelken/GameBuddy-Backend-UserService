package com.gamebuddy.user.controller;

import com.gamebuddy.user.dto.ReviewCreateDTO;
import com.gamebuddy.user.dto.ReviewViewDTO;
import com.gamebuddy.user.exception.results.DataResult;
import com.gamebuddy.user.exception.results.Result;
import com.gamebuddy.user.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
@CrossOrigin
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(summary = "Create a new review",
            description = "Creates a new review for a user and returns the created review details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Review created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<DataResult<ReviewViewDTO>> createReview(@RequestBody ReviewCreateDTO reviewCreateDTO) {
        return new ResponseEntity<>(reviewService.createReview(reviewCreateDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Get review by ID",
            description = "Retrieves the review details for the given ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review found"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    @GetMapping("/{reviewId}")
    public ResponseEntity<DataResult<ReviewViewDTO>> getReviewByReviewId(
            @Parameter(description = "ID of the review to be retrieved") @PathVariable String reviewId) {
        return new ResponseEntity<>(reviewService.getReviewByReviewId(reviewId), HttpStatus.OK);
    }

    @Operation(summary = "Delete review",
            description = "Deletes the review for the given ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Review deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Result> deleteReview(@Parameter(description = "ID of the review to be deleted") @PathVariable String reviewId) {
        return new ResponseEntity<>(reviewService.deleteReview(reviewId), HttpStatus.OK);
    }
}