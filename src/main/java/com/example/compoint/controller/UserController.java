package com.example.compoint.controller;

import com.example.compoint.entity.UserEntity;
import com.example.compoint.exception.UserAlreadyExist;
import com.example.compoint.exception.UserNotFound;
import com.example.compoint.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users")

public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Получаем список всех пользователей
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserEntity> getAllUsers() {
        return userService.getAll();
    }

    //Получаем пользователя по id
    @GetMapping("/{id}")
    public ResponseEntity getOneUser(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.getById(id));
        } catch (UserNotFound e) {
            return handleException(e);
        }
    }

    //Получаем инфу о пользователе
    @GetMapping("/info")
    @PreAuthorize("hasAuthority('ADMIN') OR hasAuthority('USER')")
    public ResponseEntity<String> userData(Principal principal) {
        return ResponseEntity.ok(principal.getName());
    }

    private ResponseEntity handleException(Exception e) {
        return ResponseEntity.badRequest().body("Ошибка: " + e.getMessage());
    }

}
