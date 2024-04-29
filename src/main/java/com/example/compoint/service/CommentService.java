package com.example.compoint.service;

import com.example.compoint.controller.CommentController;
import com.example.compoint.dtos.CommentDTO;
import com.example.compoint.dtos.StandupDTO;
import com.example.compoint.dtos.UserDTO;
import com.example.compoint.entity.CommentEntity;
import com.example.compoint.entity.RoleEntity;
import com.example.compoint.entity.StandupEntity;
import com.example.compoint.entity.UserEntity;
import com.example.compoint.repository.CommentRepo;
import com.example.compoint.repository.StandupRepo;
import com.example.compoint.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepo commentRepo;
    private final UserRepo userRepo;
    private final StandupRepo standupRepo;

    public CommentEntity create(Long standupId, Long userId, CommentEntity comment) throws Exception {
        Optional<UserEntity> user = userRepo.findById(userId);
        Optional<StandupEntity> standup = standupRepo.findById(standupId);
        if (user.isPresent() && standup.isPresent()) {
            comment.setUser(user.get());
            comment.setStandup(standup.get());
            commentRepo.save(comment);
        } else {
            throw new Exception("User or Standup not found");
        }
        return comment;
    }

    public String delete(Long commentId) throws Exception {
        Optional<CommentEntity> comment = commentRepo.findById(commentId);
        if (comment.isPresent()) {
            commentRepo.delete(comment.get());
        } else {
            throw new Exception("Comment not found");
        }
        return "Comment deleted";
    }

    public List<CommentDTO> getByStandupId(Long standupId) throws Exception {
        Optional<StandupEntity> optionalStandup = standupRepo.findById(standupId);
        if (!optionalStandup.isPresent()) {
            throw new Exception("Standup not found");
        }
        return commentRepo.findAllByStandupId(standupId).stream()
                .map(CommentDTO::new)
                .collect(Collectors.toList());
    }

    public List<CommentDTO> getByUserId(Long userId) throws Exception {
        Optional<UserEntity> optionalUser = userRepo.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new Exception("User not found");
        }
        return commentRepo.findAllByUser_Id(userId).stream()
                .map(CommentDTO::new)
                .collect(Collectors.toList());
    }

    public List<CommentDTO> getAll() {
        List<CommentEntity> comments = (List<CommentEntity>) commentRepo.findAll();
        return comments.stream()
                .map(CommentDTO::new) // Использование конструктора DTO
                .collect(Collectors.toList());
    }
}
