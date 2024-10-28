package com.gamebuddy.user.controller;

import com.gamebuddy.user.dto.ReviewCreateDTO;
import com.gamebuddy.user.dto.ReviewViewDTO;
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
    public ResponseEntity<ReviewViewDTO> createReview(@RequestBody ReviewCreateDTO reviewCreateDTO) {
        ReviewViewDTO createdReview = reviewService.createReview(reviewCreateDTO);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    @Operation(summary = "Get review by ID",
            description = "Retrieves the review details for the given ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review found"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReviewViewDTO> getReviewById(
            @Parameter(description = "ID of the review to be retrieved") @PathVariable String id) {
        ReviewViewDTO reviewViewDTO = reviewService.getReviewById(id);
        return ResponseEntity.ok(reviewViewDTO);
    }

    @Operation(summary = "Delete review",
            description = "Deletes the review for the given ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Review deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@Parameter(description = "ID of the review to be deleted") @PathVariable String id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}