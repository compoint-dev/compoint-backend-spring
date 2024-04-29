package com.example.compoint.controller;

import com.example.compoint.entity.CommentEntity;
import com.example.compoint.entity.RoleEntity;
import com.example.compoint.exception.RoleAlreadyExist;
import com.example.compoint.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/comments")
@RequiredArgsConstructor
public class CommentController {

    //TODO:ДОДЕЛАТЬ ВСЕ ЧЕПУХУ
    private final CommentService commentService;
    @PostMapping("/{userId}/create")
    public ResponseEntity createNewComment(@PathVariable Long userId, @RequestBody CommentEntity comment) {
        try {
            commentService.create(userId, comment);
            return ResponseEntity.ok("Comment created");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity getAllComments(){
        return null;
    }

    @DeleteMapping("/{commentId}/delete")
    public ResponseEntity deleteComment(@PathVariable Long commentId){
        try {
            commentService.delete(commentId);
            return ResponseEntity.ok("Comment deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
