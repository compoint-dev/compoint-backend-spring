package com.example.compoint.controller;

import com.example.compoint.entity.LanguageEntity;
import com.example.compoint.entity.StandupEntity;
import com.example.compoint.exception.AccessDenied;
import com.example.compoint.exception.StandupAlreadyExist;
import com.example.compoint.exception.StandupNotFound;
import com.example.compoint.exception.UserNotFound;
import com.example.compoint.service.LanguageService;
import com.example.compoint.service.StandupService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("api/standups")
public class StandupController {

    @Value("${compoint.uploadDir}")
    private String UPLOAD_DIR;
    private final StandupService standupService;
    private final LanguageService languageService;

    public StandupController(StandupService standupService, LanguageService languageService) {
        this.standupService = standupService;
        this.languageService = languageService;
    }

    // Create a new standup where {id} must be equal to userId
    @PostMapping("/{userId}/create")
    public ResponseEntity createStandup(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") BigDecimal price,
            @RequestParam("languages") Set<Long> languages) {
        try {
            // Сохранение файла
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            Path targetLocation = Paths.get(UPLOAD_DIR + fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Создание объекта standup
            StandupEntity standup = new StandupEntity();
            standup.setName(name);
            standup.setDescription(description);
            standup.setPrice(price);
            standup.setImagePath(targetLocation.toString());
            standup.setLanguages(languageService.getLanguagesByIds(languages));

            return ResponseEntity.ok(standupService.create(standup, userId));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
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
