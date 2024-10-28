package com.gamebuddy.user.controller;

import com.gamebuddy.user.dto.UserCreateDTO;
import com.gamebuddy.user.dto.UserUpdateDTO;
import com.gamebuddy.user.dto.UserViewDTO;
import com.gamebuddy.user.dto.auth.LoginResponse;
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
    public ResponseEntity<UserViewDTO> registerUser(@RequestBody UserCreateDTO userCreateDTO) {
        UserViewDTO createdUser = userService.registerUser(userCreateDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @Operation(summary = "Find a user by userName",
            description = "Finds user by userName and returns the user's details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/find")
    public ResponseEntity<UserViewDTO> findUser(@RequestParam String userName) {
        UserViewDTO foundUser = userService.findByUsername(userName);
        return new ResponseEntity<>(foundUser, HttpStatus.OK);
    }

    @Operation(summary = "Match password with the stored hash")
    @PostMapping("/match-password")
    public ResponseEntity<Boolean> matchPassword(@RequestParam String username, @RequestParam String password) {
        Boolean matches = userService.matchPassword(username, password);
        return new ResponseEntity<>(matches, HttpStatus.OK);
    }

    @Operation(summary = "Update user details",
            description = "Updates the user details based on the provided user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserViewDTO> updateUser(
            @Parameter(description = "ID of the user to be updated") @PathVariable String id,
            @RequestBody UserUpdateDTO userUpdateDTO) {
        UserViewDTO updatedUser = userService.updateUser(id, userUpdateDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Get user by ID",
            description = "Retrieves user details based on the provided user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserViewDTO> getUserById(
            @Parameter(description = "ID of the user to be retrieved") @PathVariable String id) {
        UserViewDTO userViewDTO = userService.getUserById(id);
        return ResponseEntity.ok(userViewDTO);
    }

    @Operation(summary = "Get all users",
            description = "Retrieves a list of all registered users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No users found")
    })
    @GetMapping
    public ResponseEntity<List<UserViewDTO>> getAllUsers() {
        List<UserViewDTO> userViewDTOs = userService.getAllUsers();
        return ResponseEntity.ok(userViewDTOs);
    }

    @Operation(summary = "Delete a user",
            description = "Deletes the user based on the provided user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID of the user to be deleted") @PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get users by criteria.",
            description = "Retrieves a list of all registered users by criteria.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No users found")
    })
    @GetMapping("/by-criteria")
    public List<UserViewDTO> getUsersByCriteria(@RequestParam(required = false) Integer minAge,
                                                @RequestParam(required = false) Integer maxAge,
                                                @RequestParam(required = false) List<String> genders) {
        return userService.getUsersByCriteria(minAge, maxAge, genders);
    }
}