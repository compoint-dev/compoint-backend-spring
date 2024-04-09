package com.example.compoint.controller;

import com.example.compoint.entity.RoleEntity;
import com.example.compoint.entity.UserEntity;
import com.example.compoint.exception.UserAlreadyExist;
import com.example.compoint.service.RoleService;
import com.example.compoint.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;
    private final UserService userService;

    public RoleController(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity createNewRole(@RequestBody RoleEntity role) {
            roleService.create(role);
            return ResponseEntity.ok("Role создан");
    }

    @GetMapping("/all")
    public ResponseEntity<List<RoleEntity>> getAllRoles() {
        List<RoleEntity> roles = roleService.getAll();
        return ResponseEntity.ok(roles);
    }

    @PostMapping("/{userId}/assign")
    public ResponseEntity<UserEntity> assignRoleToUser(@PathVariable Long userId, @RequestBody RoleEntity role) {
        UserEntity user = roleService.assignRoleToUser(userId, role);
        return ResponseEntity.ok(user);
    }
}
