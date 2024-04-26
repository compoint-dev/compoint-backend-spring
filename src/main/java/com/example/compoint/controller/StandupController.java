package com.example.compoint.controller;

import com.example.compoint.entity.StandupEntity;
import com.example.compoint.exception.AccessDenied;
import com.example.compoint.exception.StandupAlreadyExist;
import com.example.compoint.exception.StandupNotFound;
import com.example.compoint.exception.UserNotFound;
import com.example.compoint.service.StandupService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/standups")
public class StandupController {
    @Autowired
    private StandupService standupService;

    public StandupController(StandupService standupService) {
        this.standupService = standupService;
    }

    // Create a new standup where {id} must be equal to userId
    @PostMapping("/{userId}/create")
    @PreAuthorize("hasAuthority('ADMIN') or #userId == principal.id")
    public ResponseEntity createStandup(@PathVariable Long userId, @RequestBody StandupEntity standup) {

        try {
            return ResponseEntity.ok(standupService.create(standup, userId));
        } catch (StandupAlreadyExist | UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Retrieve the list of all standups
    @GetMapping("/all")
    public ResponseEntity getAllStandups() {
        try {
            return ResponseEntity.ok(standupService.getAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Retrieve the list of all standups for a specific user
    @GetMapping("/{userId}/all")
    @PreAuthorize("hasAuthority('ADMIN') or #userId == principal.id")
    public ResponseEntity getAllStandupsByUserId(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(standupService.getAllByUserId(userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Retrieve a specific standup by id
    @GetMapping("/{standupId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity getStandupById(@PathVariable Long standupId) {
        try {
            return ResponseEntity.ok(standupService.getById(standupId));
        } catch (StandupNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Retrieve a specific standup by name
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity getStandupByName(@RequestParam String name) {
        try {
            return ResponseEntity.ok(standupService.getByName(name));
        } catch (StandupNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //TODO: ДОДЕЛАТЬ ЛОГИКУ
    @PutMapping("/{id}/update")
    @PreAuthorize("hasAuthority('ADMIN') or #id == principal.id")
    public ResponseEntity updateStandup(@PathVariable Long id, @RequestBody StandupEntity standup){
        try {
            return ResponseEntity.ok(standupService.update(id, standup));
        } catch (StandupNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (StandupAlreadyExist e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // Delete a standup by id
    @DeleteMapping("/{standupId}/delete")
    public ResponseEntity deleteStandup(@PathVariable Long standupId){
        try {
            return ResponseEntity.ok(standupService.delete(standupId));
        } catch (StandupNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (AccessDenied e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
