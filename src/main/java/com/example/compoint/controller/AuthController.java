package com.example.compoint.controller;

import com.example.compoint.entity.UserEntity;
import com.example.compoint.exception.UserAlreadyExist;
import com.example.compoint.service.AuthService;
import com.example.compoint.service.UserService;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signin")
    public ResponseEntity<Void> login() {

        return null;
    }

    @PostMapping("/signup")
    public ResponseEntity createNewUser(@RequestBody UserEntity user) throws UserAlreadyExist {
        try {
            authService.create(user);
            return ResponseEntity.ok("Пользователь создан");
        } catch (UserAlreadyExist e) {
            return handleException(e);
        }

    }

    @GetMapping("/info")
    public ResponseEntity<String> getInfo(){
        return ResponseEntity.ok("RETURNED");
    }

    public record LoginRequest(String username, String password) {
    }

    private ResponseEntity handleException(Exception e) {
        return ResponseEntity.badRequest().body("Ошибка: " + e.getMessage());
    }
}
