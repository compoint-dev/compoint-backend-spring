package com.example.compoint.controller;

import com.example.compoint.dtos.CommentDTO;
import com.example.compoint.entity.CommentEntity;
import com.example.compoint.entity.RoleEntity;
import com.example.compoint.exception.RoleAlreadyExist;
import com.example.compoint.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    @PostMapping("/{standupId}/users/{userId}/comments")
    public ResponseEntity<?> createComment(@PathVariable Long standupId, @PathVariable Long userId, @RequestBody CommentEntity comment) {
        try {
            commentService.create(standupId, userId, comment);
            return ResponseEntity.ok("Comment created");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/standup")
    public ResponseEntity<?> getAllCommentsByStandupId(@RequestParam Long standupid){
        try {
            List<CommentDTO> comments = commentService.getByStandupId(standupid);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getAllCommentsByUserId(@RequestParam Long userid){
        try {
            List<CommentDTO> comments = commentService.getByUserId(userid);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllComments(){
        try {
            return ResponseEntity.ok(commentService.getAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{commentId}/delete")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId){
        try {
            commentService.delete(commentId);
            return ResponseEntity.ok("Comment deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
