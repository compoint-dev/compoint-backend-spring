package com.example.compoint.controller;

import com.example.compoint.config.UserDetailsImpl;
import com.example.compoint.dtos.StandupRequest;
import com.example.compoint.dtos.StandupResponse;
import com.example.compoint.entity.StandupEntity;
import com.example.compoint.exception.*;
import com.example.compoint.service.StandupService;
import com.example.compoint.service.WatchLaterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

//COMPLETED. NO NEED TO REFACTOR 30.05.2024
@RestController
@RequestMapping("api/standups")
@RequiredArgsConstructor
public class StandupController {

    @Value("${compoint.uploadDir}")
    private String UPLOAD_DIR;
    private final StandupService standupService;
    private final WatchLaterService watchLaterService;

    @Operation(summary = "Create a new standup", description = "Creates a new standup entry.")
    @ApiResponse(responseCode = "200", description = "Standup created successfully")
    @ApiResponse(responseCode = "404", description = "User not found or Standup already exists")
    @PostMapping("/{userid}")
    public ResponseEntity<?> createStandup(
            @Parameter(description = "ID of the user creating the standup") @PathVariable Long userid,
            @RequestBody StandupRequest standup) {
        try {
            StandupResponse createdStandup = standupService.create(standup, userid);
            return ResponseEntity.ok(createdStandup);
        } catch (StandupAlreadyExist | UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Retrieve all standups", description = "Retrieves all standups available in the system.")
    @ApiResponse(responseCode = "200", description = "List of all standups retrieved successfully")
    @GetMapping
    public ResponseEntity<?> getAllStandups() {
        try {
            return ResponseEntity.ok(standupService.getAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Retrieve standups by user ID", description = "Retrieves all standups associated with a specific user.")
    @ApiResponse(responseCode = "200", description = "List of standups retrieved successfully")
    @GetMapping("/user/{userid}")
    public ResponseEntity<?> getAllStandupsByUserId(
            @Parameter(description = "ID of the user creating the standup") @PathVariable Long userid) {
        try {
            return ResponseEntity.ok(standupService.getAllByUserId(userid));
        } catch (UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Retrieve a standup by ID", description = "Retrieves details of a specific standup by its ID.")
    @ApiResponse(responseCode = "200", description = "Standup details retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Standup not found")
    @GetMapping("/{id}")
    public ResponseEntity<?> getStandupById(
            @Parameter(description = "ID of the specific standup") @PathVariable Long id) {
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
    public ResponseEntity<?> getStandupByName(
            @Parameter(description = "Name of the specific standup") @RequestParam(required = false) String name) {
        try {
            return ResponseEntity.ok(standupService.getByName(name));
        } catch (StandupNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

//    @Operation(summary = "Retrieve top standups of the day", description = "Retrieves the top standups of the current day.")
//    @ApiResponse(responseCode = "200", description = "Top standups of the day retrieved successfully")
//    @GetMapping("/top/day")
//    public ResponseEntity<?> getTopDayStandup() {
//        return ResponseEntity.ok(standupService.getTopDay());
//    }
//
//    @Operation(summary = "Retrieve top standups of the month", description = "Retrieves the top standups of the current month.")
//    @ApiResponse(responseCode = "200", description = "Top standups of the month retrieved successfully")
//    @GetMapping("/top/month")
//    public ResponseEntity<?> getTopMonthStandup() {
//        return ResponseEntity.ok(standupService.getTopMonth());
//    }
//
//    @Operation(summary = "Retrieve top standups of the year", description = "Retrieves the top standups of the current year.")
//    @ApiResponse(responseCode = "200", description = "Top standups of the year retrieved successfully")
//    @GetMapping("/top/year")
//    public ResponseEntity<?> getTopYearStandup() {
//        return ResponseEntity.ok(standupService.getTopYear());
//    }

    //TODO: Доделать
    @Operation(summary = "Update a standup", description = "Updates details of an existing standup.")
    @ApiResponse(responseCode = "200", description = "Standup updated successfully")
    @ApiResponse(responseCode = "404", description = "Standup not found or already exists")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStandup(
            @Parameter(description = "ID of the specific standup") @PathVariable Long id,
            @RequestBody StandupEntity standup) {
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
    public ResponseEntity<?> deleteStandup(
            @Parameter(description = "ID of the specific standup") @PathVariable Long id) {
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
    public ResponseEntity<?> addToWatchLater(
            @Parameter(description = "ID of the specific standup") @PathVariable Long id,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        try {
            return ResponseEntity.ok(watchLaterService.addToWatchLater(id, userDetails.getId()));
        } catch (StandupNotFound | UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (AlreadyWatchLater e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

    }

    @Operation(summary = "Remove from watch later", description = "Removes a standup from the watch later list for the authenticated user.")
    @ApiResponse(responseCode = "200", description = "Standup removed from watch later successfully")
    @DeleteMapping("{id}/watch-later")
    public ResponseEntity<?> removeFromWatchLater(
            @Parameter(description = "ID of the specific standup") @PathVariable Long id,
            Authentication authentication) throws UserNotFound, StandupNotFound {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        try {
            return ResponseEntity.ok(watchLaterService.removeFromWatchLater(id, userDetails.getId()));
        } catch (StandupNotFound | UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
