package com.gamebuddy.user.controller;

import com.gamebuddy.user.dto.FriendCreateDTO;
import com.gamebuddy.user.dto.UserCreateDTO;
import com.gamebuddy.user.dto.UserUpdateDTO;
import com.gamebuddy.user.dto.UserViewDTO;
import com.gamebuddy.user.dto.auth.RegisterResponse;
import com.gamebuddy.user.exception.results.DataResult;
import com.gamebuddy.user.exception.results.Result;
import com.gamebuddy.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Register a new user",
            description = "Creates a new user and returns the created user's details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/register")
    public ResponseEntity<DataResult<RegisterResponse>> registerUser(@RequestBody UserCreateDTO userCreateDTO) {
        return new ResponseEntity<>(userService.registerUser(userCreateDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Find a user by userName",
            description = "Finds user by userName and returns the user's details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/findUser")
    public ResponseEntity<DataResult<UserViewDTO>> findUser(@RequestParam String userName) {
        return new ResponseEntity<>(userService.findByUsername(userName), HttpStatus.OK);
    }

    @Operation(summary = "Match password with the stored hash")
    @PostMapping("/match-password")
    public ResponseEntity<Result> matchPassword(@RequestParam String userName, @RequestParam String password) {
        return new ResponseEntity<>(userService.matchPassword(userName, password), HttpStatus.OK);
    }

    @Operation(summary = "Update user details",
            description = "Updates the user details based on the provided user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("/{userId}")
    public ResponseEntity<DataResult<UserViewDTO>> updateUser(
            @Parameter(description = "ID of the user to be updated") @PathVariable String userId,
            @RequestBody UserUpdateDTO userUpdateDTO) {
        return new ResponseEntity<>(userService.updateUser(userId, userUpdateDTO), HttpStatus.OK);
    }

    @Operation(summary = "Get user by ID",
            description = "Retrieves user details based on the provided user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<DataResult<UserViewDTO>> getUserById(
            @Parameter(description = "ID of the user to be retrieved") @PathVariable String userId) {
        return new ResponseEntity<>(userService.getUserByUserId(userId), HttpStatus.OK);
    }

    @Operation(summary = "Get all users",
            description = "Retrieves a list of all registered users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No users found")
    })
    @GetMapping
    public ResponseEntity<DataResult<List<UserViewDTO>>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @Operation(summary = "Delete a user",
            description = "Deletes the user based on the provided user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<Result> deleteUser(
            @Parameter(description = "ID of the user to be deleted") @PathVariable String userId) {
        return new ResponseEntity<>(userService.deleteUser(userId), HttpStatus.OK);
    }

    @Operation(summary = "Get users by criteria.",
            description = "Retrieves a list of all registered users by criteria.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No users found")
    })
    @GetMapping("/by-criteria")
    public ResponseEntity<DataResult<List<UserViewDTO>>> getUsersByCriteria(@RequestParam(required = false) Integer minAge,
                                                                            @RequestParam(required = false) Integer maxAge,
                                                                            @RequestParam(required = false) Float minRating,
                                                                            @RequestParam(required = false) Float maxRating,
                                                                            @RequestParam(required = false) List<String> genders) {
        return new ResponseEntity<>(userService.getUsersByCriteria(minAge, maxAge, minRating, maxRating, genders), HttpStatus.OK);
    }

    @Operation(summary = "Add friend",
            description = "Adds the friend.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Friend added successfully"),
    })
    @PostMapping("/{userId}/add-friend")
    public ResponseEntity<Result> addFriend(@PathVariable String userId, @RequestBody FriendCreateDTO friendCreateDTO) {
        return new ResponseEntity<>(userService.addFriend(userId, friendCreateDTO), HttpStatus.OK);
    }
}