package com.example.compoint.controller;

import com.example.compoint.dtos.CommentDTO;
import com.example.compoint.dtos.CreateCommentDTO;
import com.example.compoint.exception.CommentNotFound;
import com.example.compoint.exception.StandupNotFound;
import com.example.compoint.exception.UserNotFound;
import com.example.compoint.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//COMPLETED. NO NEED TO REFACTOR 30.05.2024
@RestController
@RequestMapping("api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "Create a comment", description = "Creates a new comment on a specific standup by a user.")
    @ApiResponse(responseCode = "200", description = "Comment created successfully")
    @ApiResponse(responseCode = "404", description = "User or Standup not found")
    @PostMapping("/{standupid}/user/{userid}/comments")
    public ResponseEntity<?> createComment(
            @Parameter(description = "ID of the standup to comment on") @PathVariable Long standupid,
            @Parameter(description = "ID of the user creating the comment") @PathVariable Long userid,
            @RequestBody CreateCommentDTO commentDTO) {
        try {
            CommentDTO createdComment = commentService.create(standupid, userid, commentDTO);
            return ResponseEntity.ok(createdComment);
        } catch (StandupNotFound | UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Get all comments by standup ID", description = "Retrieves all comments made on a specific standup.")
    @ApiResponse(responseCode = "200", description = "Comments retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Standup not found")
    @GetMapping("/standup")
    public ResponseEntity<?> getAllCommentsByStandupId(
            @Parameter(description = "ID of the standup to retrieve comments for") @RequestParam Long standupid) {
        try {
            List<CommentDTO> comments = commentService.getByStandupId(standupid);
            return ResponseEntity.ok(comments);
        } catch (StandupNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Get all comments by user ID", description = "Retrieves all comments made by a specific user.")
    @ApiResponse(responseCode = "200", description = "Comments retrieved successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/user")
    public ResponseEntity<?> getAllCommentsByUserId(
            @Parameter(description = "ID of the user to retrieve comments for") @RequestParam Long userid) {
        try {
            List<CommentDTO> comments = commentService.getByUserId(userid);
            return ResponseEntity.ok(comments);
        } catch (UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Get all comments", description = "Retrieves all comments.")
    @ApiResponse(responseCode = "200", description = "All comments retrieved successfully")
    @GetMapping
    public ResponseEntity<?> getAllComments() {
        try {
            return ResponseEntity.ok(commentService.getAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Delete a comment", description = "Deletes a comment by its ID.")
    @ApiResponse(responseCode = "200", description = "Comment deleted successfully")
    @ApiResponse(responseCode = "404", description = "Comment not found")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteComment(
            @Parameter(description = "ID of the comment to delete") @PathVariable Long id) {
        try {
            commentService.delete(id);
            return ResponseEntity.ok("Comment deleted");
        } catch (CommentNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Change rating of a comment", description = "Allows a user to change the rating of a comment.")
    @ApiResponse(responseCode = "200", description = "Rating updated successfully")
    @ApiResponse(responseCode = "404", description = "Comment or User not found")
    @PostMapping("/rating/{commentId}/user/{userId}")
    public ResponseEntity<?> changeRating(
            @Parameter(description = "ID of the comment") @PathVariable Long commentId,
            @Parameter(description = "ID of the user") @PathVariable Long userId,
            @RequestBody String value) {
        try {
            commentService.changeRating(commentId, userId, value);
            return ResponseEntity.ok("Rating updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
