package com.example.compoint.controller;

import com.example.compoint.config.UserDetailsImpl;
import com.example.compoint.entity.UserEntity;
import com.example.compoint.exception.UserNotFound;
import com.example.compoint.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity getAllUsers() {
        List<UserEntity> users = userService.getAll();
        return ResponseEntity.ok(users);
    }

    //Получаем пользователя по id
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.getById(id));
        } catch (UserNotFound e) {
            return handleException(e);
        }
    }

    @GetMapping("/username")
    @PreAuthorize("hasAuthority('ADMIN') or #username == principal.username")
    public ResponseEntity getUserByUsername(@RequestParam String username, Principal principal) {
        try {
            return ResponseEntity.ok(userService.getByUsername(username,principal));
        } catch (UserNotFound e) {
            return handleException(e);
        }
    }

    @GetMapping("/info")
    @PreAuthorize("hasAuthority('ADMIN') OR hasAuthority('USER')")
    public ResponseEntity<String> userData(Principal principal) {
        return ResponseEntity.ok(principal.getName());
    }

    private ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.badRequest().body("Ошибка: " + e.getMessage());
    }

}
