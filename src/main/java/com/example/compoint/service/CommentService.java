package com.example.compoint.service;

import com.example.compoint.entity.CommentEntity;
import com.example.compoint.entity.UserEntity;
import com.example.compoint.repository.CommentRepo;
import com.example.compoint.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepo commentRepo;
    private final UserRepo userRepo;

    public CommentEntity create(Long userId, CommentEntity comment) throws Exception {
        Optional<UserEntity> user = userRepo.findById(userId);
        if (user.isPresent()) {
            comment.setUser(user.get());
            commentRepo.save(comment);
        } else {
            throw new Exception("User not found");
        }
        return comment;
    }

    public String delete(Long commentId) throws Exception {
        Optional<CommentEntity> comment = commentRepo.findById(commentId);
        if (comment.isPresent()){
            commentRepo.delete(comment.get());
        } else {
            throw new Exception("Comment not found");
        }
        return "Comment deleted";
    }
}
