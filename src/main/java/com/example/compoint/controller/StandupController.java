package com.example.compoint.controller;

import com.example.compoint.entity.StandupEntity;
import com.example.compoint.exception.StandupAlreadyExist;
import com.example.compoint.exception.StandupNotFound;
import com.example.compoint.exception.UserNotFound;
import com.example.compoint.service.StandupService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("{id}/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity createStandup(@PathVariable Long id, @RequestBody StandupEntity standup) {
        try {
            return ResponseEntity.ok(standupService.create(standup, id));
        } catch (StandupAlreadyExist | UserNotFound e) {
            return handleException(e);
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity getAllStandups() {
        try {
            return ResponseEntity.ok(standupService.getAll());
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity getStandupById(@RequestParam Long id) {
        try {
            return ResponseEntity.ok(standupService.getById(id));
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity getStandupByName(@RequestParam String name) {
        try {
            return ResponseEntity.ok(standupService.getByName(name));
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @DeleteMapping("{id}/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity deleteStandup(@PathVariable Long id){
        try {
            return ResponseEntity.ok(standupService.delete(id));
        } catch (StandupNotFound e) {
            return handleException(e);
        }
    }

    private ResponseEntity handleException(Exception e) {
        return ResponseEntity.badRequest().body("Ошибка: " + e.getMessage());
    }

}
