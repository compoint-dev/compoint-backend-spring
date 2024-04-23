package com.example.compoint.controller;

import com.example.compoint.config.UserDetailsImpl;
import com.example.compoint.entity.RoleEntity;
import com.example.compoint.entity.UserEntity;
import com.example.compoint.exception.RoleNotFound;
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
        } catch (UserAlreadyExist | RoleNotFound e) {
            return handleException(e);
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
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.getById(id));
        } catch (UserNotFound e) {
            return handleException(e);
        }
    }

    //Получаем пользователя по username
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity getUserByUsername(@RequestParam String username) {
        try {
            return ResponseEntity.ok(userService.getByUsername(username));
        } catch (UserNotFound e) {
            return handleException(e);
        }
    }

    @PutMapping("{id}/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity updateUser(@PathVariable Long id, @RequestBody UserEntity user){
        try {
            return ResponseEntity.ok(userService.update(id, user));
        } catch (UserNotFound | UserAlreadyExist e) {
            return handleException(e);
        }
    }

    @DeleteMapping("{id}/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity deleteUser(@PathVariable Long id){
        try {
            return ResponseEntity.ok(userService.delete(id));
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
        return ResponseEntity.badRequest().body("Error: " + e.getMessage());
    }

}
