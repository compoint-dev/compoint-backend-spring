package com.example.compoint.controller;

import com.example.compoint.config.UserDetailsImpl;
import com.example.compoint.entity.StandupEntity;
import com.example.compoint.exception.AccessDenied;
import com.example.compoint.exception.StandupAlreadyExist;
import com.example.compoint.exception.StandupNotFound;
import com.example.compoint.exception.UserNotFound;
import com.example.compoint.service.LanguageService;
import com.example.compoint.service.StandupService;
import com.example.compoint.service.WatchLaterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;

@RestController
@RequestMapping("api/standups")
public class StandupController {

    @Value("${compoint.uploadDir}")
    private String UPLOAD_DIR;
    private final StandupService standupService;
    private final LanguageService languageService;
    private final WatchLaterService watchLaterService;

    public StandupController(StandupService standupService, LanguageService languageService, WatchLaterService watchLaterService) {
        this.standupService = standupService;
        this.languageService = languageService;
        this.watchLaterService = watchLaterService;
    }

    @Operation(summary = "Create a new standup", description = "Creates a new standup entry with file upload.")
    @ApiResponse(responseCode = "200", description = "Standup created successfully")
    @ApiResponse(responseCode = "404", description = "User not found or Standup already exists")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    @PostMapping("/{userid}")
    public ResponseEntity<?> createStandup(
            @Parameter(description = "ID of the user creating the standup") @PathVariable Long userid,
            @Parameter(description = "Image for standup") @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") BigDecimal price,
            @RequestParam("languages") Set<Long> languages) {
        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            Path targetLocation = Paths.get(UPLOAD_DIR + fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            StandupEntity standup = new StandupEntity();
            standup.setName(name);
            standup.setDescription(description);
            standup.setPrice(price);
            standup.setImagePath(targetLocation.toString());
            standup.setLanguages(languageService.getLanguagesByIds(languages));

            return ResponseEntity.ok(standupService.create(standup, userid));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (StandupAlreadyExist | UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Retrieve all standups", description = "Retrieves all standups available in the system.")
    @ApiResponse(responseCode = "200", description = "List of all standups retrieved successfully")
    @GetMapping
    public ResponseEntity<?> getAllStandups() {
        return ResponseEntity.ok(standupService.getAll());
    }

    @Operation(summary = "Retrieve standups by user ID", description = "Retrieves all standups associated with a specific user.")
    @ApiResponse(responseCode = "200", description = "List of standups retrieved successfully")
    @GetMapping("/user/{userid}")
    public ResponseEntity<?> getAllStandupsByUserId(@PathVariable Long userid) {
        return ResponseEntity.ok(standupService.getAllByUserId(userid));
    }

    @Operation(summary = "Retrieve a standup by ID", description = "Retrieves details of a specific standup by its ID.")
    @ApiResponse(responseCode = "200", description = "Standup details retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Standup not found")
    @GetMapping("/{id}")
    public ResponseEntity<?> getStandupById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(standupService.getById(id));
        } catch (StandupNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Retrieve a standup by name", description = "Retrieves standup details by its name.")
    @ApiResponse(responseCode = "200", description = "Standup retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Standup not found")
    @GetMapping(params = "name")
    public ResponseEntity<?> getStandupByName(@RequestParam(required = false) String name) {
        try {
            return ResponseEntity.ok(standupService.getByName(name));
        } catch (StandupNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Retrieve top standups of the day", description = "Retrieves the top standups of the current day.")
    @ApiResponse(responseCode = "200", description = "Top standups of the day retrieved successfully")
    @GetMapping("/top/day")
    public ResponseEntity<?> getTopDayStandup() {
        return ResponseEntity.ok(standupService.getTopDay());
    }

    @Operation(summary = "Retrieve top standups of the month", description = "Retrieves the top standups of the current month.")
    @ApiResponse(responseCode = "200", description = "Top standups of the month retrieved successfully")
    @GetMapping("/top/month")
    public ResponseEntity<?> getTopMonthStandup() {
        return ResponseEntity.ok(standupService.getTopMonth());
    }

    @Operation(summary = "Retrieve top standups of the year", description = "Retrieves the top standups of the current year.")
    @ApiResponse(responseCode = "200", description = "Top standups of the year retrieved successfully")
    @GetMapping("/top/year")
    public ResponseEntity<?> getTopYearStandup() {
        return ResponseEntity.ok(standupService.getTopYear());
    }

    @Operation(summary = "Update a standup", description = "Updates details of an existing standup.")
    @ApiResponse(responseCode = "200", description = "Standup updated successfully")
    @ApiResponse(responseCode = "404", description = "Standup not found or already exists")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStandup(@PathVariable Long id, @RequestBody StandupEntity standup) {
        try {
            return ResponseEntity.ok(standupService.update(id, standup));
        } catch (StandupNotFound | StandupAlreadyExist e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Delete a standup", description = "Deletes an existing standup.")
    @ApiResponse(responseCode = "200", description = "Standup deleted successfully")
    @ApiResponse(responseCode = "404", description = "Standup not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStandup(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(standupService.delete(id));
        } catch (StandupNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (AccessDenied e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @Operation(summary = "Add to watch later", description = "Marks a standup to watch later for the authenticated user.")
    @ApiResponse(responseCode = "200", description = "Standup marked as watch later successfully")
    @PostMapping("{id}/watch-later")
    public ResponseEntity<?> addToWatchLater(@PathVariable Long id, Authentication authentication) throws Exception {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(watchLaterService.addToWatchLater(id, userDetails.getId()));
    }

    @Operation(summary = "Remove from watch later", description = "Removes a standup from the watch later list for the authenticated user.")
    @ApiResponse(responseCode = "200", description = "Standup removed from watch later successfully")
    @DeleteMapping("{id}/watch-later")
    public ResponseEntity<?> removeFromWatchLater(@PathVariable Long id, Authentication authentication) throws UserNotFound, StandupNotFound {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(watchLaterService.removeFromWatchLater(id, userDetails.getId()));
    }
}
