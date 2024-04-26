package com.example.compoint.controller;

import com.example.compoint.entity.RoleEntity;
import com.example.compoint.entity.UserEntity;
import com.example.compoint.exception.RoleAlreadyExist;
import com.example.compoint.service.RoleService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    // Create a new role
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity createNewRole(@RequestBody RoleEntity role) {
        try {
            roleService.create(role);
            return ResponseEntity.ok("Role created");
        } catch (RoleAlreadyExist e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Retrieve the list of all roles
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity getAllRoles() {
        List<RoleEntity> roles = roleService.getAll();
        return ResponseEntity.ok(roles);
    }

    // Assign a role to a specific user
    @PostMapping("/{userId}/assign")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity assignRoleToUser(@PathVariable Long userId, @RequestBody RoleEntity role) {
        UserEntity user = roleService.assignRoleToUser(userId, role);
        return ResponseEntity.ok(user);
    }
}
