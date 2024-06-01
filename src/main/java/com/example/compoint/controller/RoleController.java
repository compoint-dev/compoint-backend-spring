package com.example.compoint.controller;

import com.example.compoint.dtos.RoleRequest;
import com.example.compoint.dtos.UserDTO;
import com.example.compoint.entity.RoleEntity;
import com.example.compoint.exception.RoleAlreadyExist;
import com.example.compoint.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/roles")
@RequiredArgsConstructor
public class    RoleController {

    private final RoleService roleService;

    @Operation(summary = "Create a new role", description = "Creates a new role in the system. Only accessible by ADMIN users.")
    @ApiResponse(responseCode = "200", description = "Role created successfully")
    @ApiResponse(responseCode = "409", description = "Role already exists")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createNewRole(@RequestBody RoleRequest role) {
        try {
            roleService.create(role);
            return ResponseEntity.ok("Role created");
        } catch (RoleAlreadyExist e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @Operation(summary = "Get all roles", description = "Retrieves all roles available in the system. Only accessible by ADMIN users.")
    @ApiResponse(responseCode = "200", description = "All roles retrieved successfully")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllRoles() {
        return ResponseEntity.ok(roleService.getAll());
    }

    @Operation(summary = "Assign role to user", description = "Assigns a specified role to a user. Only accessible by ADMIN users.")
    @ApiResponse(responseCode = "200", description = "Role assigned to user successfully")
    @ApiResponse(responseCode = "404", description = "User or role not found")
    @PostMapping("/{userid}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> assignRoleToUser(
            @Parameter(description = "ID of the user to whom the role is to be assigned") @PathVariable Long userid,
            @RequestBody RoleRequest role) {
        try {
            return ResponseEntity.ok(roleService.assignRoleToUser(userid, role));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
