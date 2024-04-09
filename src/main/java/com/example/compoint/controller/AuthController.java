package com.example.compoint.controller;

import com.example.compoint.entity.UserEntity;
import com.example.compoint.exception.RoleNotFound;
import com.example.compoint.exception.UserAlreadyExist;
import com.example.compoint.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signin")
    public String signin() {
        return "Check";
    }

    @PostMapping("/signup")
    public ResponseEntity createNewUser(@RequestBody UserEntity user) throws UserAlreadyExist, RoleNotFound {
        try {
            authService.create(user);
            return ResponseEntity.ok("Пользователь создан");
        } catch (UserAlreadyExist e) {
            return handleException(e);
        }

    }

    private ResponseEntity handleException(Exception e) {
        return ResponseEntity.badRequest().body("Ошибка: " + e.getMessage());
    }
}
