package com.example.compoint.controller;

import com.example.compoint.entity.RoleEntity;
import com.example.compoint.entity.UserEntity;
import com.example.compoint.exception.RoleAlreadyExist;
import com.example.compoint.exception.RoleNotFound;
import com.example.compoint.exception.UserAlreadyExist;
import com.example.compoint.service.RoleService;
import com.example.compoint.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;
    private final UserService userService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity createNewRole(@RequestBody RoleEntity role) throws RoleAlreadyExist {
        try {
            roleService.create(role);
            return ResponseEntity.ok("Role created");
        } catch (RoleAlreadyExist e) {
            return handleException(e);
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity getAllRoles() {
        List<RoleEntity> roles = roleService.getAll();
        return ResponseEntity.ok(roles);
    }

    @PostMapping("/{userId}/assign")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity assignRoleToUser(@PathVariable Long userId, @RequestBody RoleEntity role) {
        UserEntity user = roleService.assignRoleToUser(userId, role);
        return ResponseEntity.ok(user);
    }

    private ResponseEntity handleException(Exception e) {
        return ResponseEntity.badRequest().body("Error: " + e.getMessage());
    }
}
