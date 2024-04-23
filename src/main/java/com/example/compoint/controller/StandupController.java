package com.example.compoint.controller;

import com.example.compoint.entity.StandupEntity;
import com.example.compoint.service.StandupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/standups")
public class StandupController {
    @Autowired
    private StandupService standupService;

    public StandupController(StandupService standupService) {
        this.standupService = standupService;
    }

//    @PostMapping("/create")
//    public ResponseEntity createStandup(@RequestParam Long userId, @RequestBody StandupEntity standup) {
//        try {
//            standupService.create(standup, userId);
//            return ResponseEntity.ok("Стендап успешно создан");
//        } catch (Exception e) {
//            return handleException(e);
//        }
//    }

    @GetMapping(params = "name")
    public ResponseEntity getOneStandup(@RequestParam String name) {
        try {
            return ResponseEntity.ok(standupService.getOne(name));
        } catch (Exception e) {
            return handleException(e);
        }
    }
    @GetMapping(params = "id")
    public ResponseEntity getOneStandup(@RequestParam Long id) {
        try {
            return ResponseEntity.ok(standupService.getOne(id));
        } catch (Exception e) {
            return handleException(e);
        }
    }
    @GetMapping("/all")
    public ResponseEntity getAllStandups() {
        try {
            return ResponseEntity.ok(standupService.getAll());
        } catch (Exception e) {
            return handleException(e);
        }
    }

    private ResponseEntity handleException(Exception e) {
        return ResponseEntity.badRequest().body("Ошибка: " + e.getMessage());
    }

}
