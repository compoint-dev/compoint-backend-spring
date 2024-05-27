package com.example.compoint.controller;

import com.example.compoint.service.StandupInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/standupinfo")
public class StandupInfoController {

    private final StandupInfoService standupInfoService;

    public StandupInfoController(StandupInfoService standupInfoService) {
        this.standupInfoService = standupInfoService;
    }

    @PostMapping("/rating/{id}")
    public ResponseEntity<?> changeRating(@PathVariable Long id, @RequestBody String value) {
        try {
            standupInfoService.change(id, value);
            return ResponseEntity.ok("Rating updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating rating");
        }
    }
}
