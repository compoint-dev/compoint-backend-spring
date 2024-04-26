package com.example.compoint.controller;

import com.example.compoint.config.UserDetailsImpl;
import com.example.compoint.entity.RoleEntity;
import com.example.compoint.entity.UserEntity;
import com.example.compoint.exception.RoleNotFound;
import com.example.compoint.exception.StandupAlreadyExist;
import com.example.compoint.exception.UserAlreadyExist;
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
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //Создаем нового юзера
    @PostMapping("/create")
    public ResponseEntity createUser(@RequestBody UserEntity user){
        try {
            return ResponseEntity.ok(userService.create(user));
        } catch (UserAlreadyExist e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RoleNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //Получаем всех пользователей
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity getAllUsers() {
        List<UserEntity> users = userService.getAll();
        return ResponseEntity.ok(users);
    }

    //Получаем пользователя по id
    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN') or #userId == principal.id")
    public ResponseEntity getUserById(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(userService.getById(userId));
        } catch (UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //Получаем пользователя по username
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity getUserByUsername(@RequestParam String username) {
        try {
            return ResponseEntity.ok(userService.getByUsername(username));
        } catch (UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{userId}/update")
    @PreAuthorize("hasAuthority('ADMIN') or #userId == principal.id")
    public ResponseEntity updateUser(@PathVariable Long userId, @RequestBody UserEntity user){
        try {
            return ResponseEntity.ok(userService.update(userId, user));
        } catch (UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UserAlreadyExist e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity deleteUser(@PathVariable Long id){
        try {
            return ResponseEntity.ok(userService.delete(id));
        } catch (UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/info")
    @PreAuthorize("hasAuthority('ADMIN') OR hasAuthority('USER')")
    public ResponseEntity<String> userData(Principal principal) {
        return ResponseEntity.ok(principal.getName());
    }

}
