package com.example.compoint.controller;

import com.example.compoint.entity.StandupInfoEntity;
import com.example.compoint.service.StandupInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/standupinfo")
@RequiredArgsConstructor
public class StandupInfoController {

    private final StandupInfoService standupInfoService;

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStandupInfo(@PathVariable Long id, @RequestBody StandupInfoEntity standupInfo) {
        try {
            return ResponseEntity.ok(standupInfoService.update(id, standupInfo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }

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
