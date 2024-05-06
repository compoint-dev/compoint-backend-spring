package com.example.compoint.controller;

import com.example.compoint.entity.UserEntity;
import com.example.compoint.exception.RoleNotFound;
import com.example.compoint.exception.UserAlreadyExist;
import com.example.compoint.exception.UserNotAuthorized;
import com.example.compoint.exception.UserNotFound;
import com.example.compoint.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserEntity user){
        try {
            return ResponseEntity.ok(userService.create(user));
        } catch (UserAlreadyExist e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RoleNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        List<UserEntity> users = userService.getAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
//    @PreAuthorize("hasAuthority('ADMIN') or #userId == principal.id")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(userService.getById(userId));
        } catch (UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping(params = "username")
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getUserByUsername(@RequestParam String username) {
        try {
            return ResponseEntity.ok(userService.getByUsername(username));
        } catch (UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{userId}")
//    @PreAuthorize("hasAuthority('ADMIN') or #userId == principal.id")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody UserEntity user){
        try {
            return ResponseEntity.ok(userService.update(userId, user));
        } catch (UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UserAlreadyExist e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/{userId}")
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        try {
            return ResponseEntity.ok(userService.delete(id));
        } catch (UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        try {
            return ResponseEntity.ok(userService.getCurrentUser(authentication));
        }  catch (UserNotAuthorized e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/info")
//    @PreAuthorize("hasAuthority('ADMIN') OR hasAuthority('USER')")
    public ResponseEntity<?> userData(Principal principal) {
        return ResponseEntity.ok(principal.getName());
    }

}
