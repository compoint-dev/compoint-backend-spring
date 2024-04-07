package com.example.compoint.controller;

import com.example.compoint.entity.UserEntity;
import com.example.compoint.exception.UserAlreadyExist;
import com.example.compoint.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

//    @PostMapping("/signin")
//    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest) {
//        Authentication authenticationRequest =
//                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
//        Authentication authenticationResponse =
//                this.authenticationManager.authenticate(authenticationRequest);
//
//        SecurityContextHolder.getContext().setAuthentication(authenticationResponse);
//
//        // Дополнительная логика обработки успешной аутентификации
//
//        return ResponseEntity.ok().build();
//    }

    public static record LoginRequest(String username, String password) {

        public String getUsername() {
            return username;
        }
        public String getPassword() {
            return password;
        }
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

    private ResponseEntity handleException(Exception e) {
        return ResponseEntity.badRequest().body("Ошибка: " + e.getMessage());
    }
}
