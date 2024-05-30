package com.example.compoint.controller;

import com.example.compoint.dtos.UserSignupRequest;
import com.example.compoint.entity.UserEntity;
import com.example.compoint.exception.RoleNotFound;
import com.example.compoint.exception.UserAlreadyExist;
import com.example.compoint.exception.UserNotAuthorized;
import com.example.compoint.exception.UserNotFound;
import com.example.compoint.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Create a new user", description = "Creates a new user and returns it")
    @ApiResponse(responseCode = "200", description = "User created successfully")
    @ApiResponse(responseCode = "409", description = "User already exists")
    @ApiResponse(responseCode = "404", description = "Role not found")
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserSignupRequest user) {
        try {
            return ResponseEntity.ok(userService.create(user));
        } catch (UserAlreadyExist e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RoleNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Retrieve all users", description = "Gets a list of all users")
    @ApiResponse(responseCode = "200", description = "List of users retrieved successfully")
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAll());
    }

    @Operation(summary = "Get a user by ID", description = "Gets user details by user ID")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@Parameter(description = "ID of the user to be retrieved") @PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.getById(id));
        } catch (UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Get user by username", description = "Gets user details by username")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping(params = "username")
    public ResponseEntity<?> getUserByUsername(@Parameter(description = "Username of the user") @RequestParam(required = false) String username) {
        try {
            return ResponseEntity.ok(userService.getByUsername(username));
        } catch (UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Update a user", description = "Updates a user's details")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "409", description = "User already exists with updated details")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserEntity user) {
        try {
            return ResponseEntity.ok(userService.update(id, user));
        } catch (UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UserAlreadyExist e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @Operation(summary = "Delete a user", description = "Deletes a user by ID")
    @ApiResponse(responseCode = "200", description = "User deleted successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.delete(id));
        } catch (UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Get current user", description = "Retrieves details of the currently authenticated user")
    @ApiResponse(responseCode = "200", description = "Current user details retrieved")
    @ApiResponse(responseCode = "401", description = "User not authorized")
    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        try {
            return ResponseEntity.ok(userService.getCurrentUser(authentication));
        } catch (UserNotAuthorized e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @Operation(summary = "Get user data", description = "Gets data of the currently authenticated user")
    @ApiResponse(responseCode = "200", description = "User data retrieved successfully")
    @GetMapping("/info")
    public ResponseEntity<?> userData(Principal principal) {
        return ResponseEntity.ok(principal.getName());
    }

}
