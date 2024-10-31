package com.gamebuddy.user.controller;

import com.gamebuddy.user.dto.FriendShipViewDTO;
import com.gamebuddy.user.exception.results.DataResult;
import com.gamebuddy.user.service.FriendShipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/friendships")
@CrossOrigin
public class FriendShipController {

    private final FriendShipService friendShipService;

    public FriendShipController(FriendShipService friendShipService) {
        this.friendShipService = friendShipService;
    }

    @Operation(summary = "Get all friendships",
            description = "Retrieves a list of all friendships.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friendships retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No friendships found")
    })
    @GetMapping
    public ResponseEntity<DataResult<List<FriendShipViewDTO>>> getAllFriendShips() {
        return new ResponseEntity<>(friendShipService.getAllFriendShips(), HttpStatus.OK);
    }
}
