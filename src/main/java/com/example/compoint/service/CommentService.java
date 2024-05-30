package com.example.compoint.service;

import com.example.compoint.dtos.CommentResponse;
import com.example.compoint.dtos.CommentRequest;
import com.example.compoint.entity.CommentEntity;
import com.example.compoint.entity.CommentRatingEntity;
import com.example.compoint.entity.StandupEntity;
import com.example.compoint.entity.UserEntity;
import com.example.compoint.exception.CommentNotFound;
import com.example.compoint.exception.StandupNotFound;
import com.example.compoint.exception.UserNotFound;
import com.example.compoint.mappers.CommentMapper;
import com.example.compoint.repository.CommentRatingRepo;
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
    private final CommentRatingRepo commentRatingRepo;

    public CommentResponse create(Long standupId, Long userId, CommentRequest createCommentDTO) throws UserNotFound, StandupNotFound {
        UserEntity user = userRepo.findById(userId).orElseThrow(() -> new UserNotFound("User not found"));
        StandupEntity standup = standupRepo.findById(standupId).orElseThrow(() -> new StandupNotFound("Standup not found"));

        CommentEntity commentEntity = CommentMapper.INSTANCE.commentRequestToCommentEntity(createCommentDTO);
        commentEntity.setUser(user);
        commentEntity.setStandup(standup);
        commentEntity.setRating(0);

        commentRepo.save(commentEntity);

        return CommentMapper.INSTANCE.commentEntityToCommentResponse(commentEntity);
    }

    public String delete(Long commentId) throws CommentNotFound {
        Optional<CommentEntity> comment = commentRepo.findById(commentId);
        if (comment.isPresent()) {
            commentRepo.delete(comment.get());
        } else {
            throw new CommentNotFound("Comment not found");
        }
        return "Comment deleted";
    }

    public List<CommentResponse> getByStandupId(Long standupId) throws StandupNotFound {
        Optional<StandupEntity> optionalStandup = standupRepo.findById(standupId);
        if (!optionalStandup.isPresent()) {
            throw new StandupNotFound("Standup not found");
        }

        return commentRepo.findAllByStandupId(standupId).stream()
                .map(CommentMapper.INSTANCE::commentEntityToCommentResponse)
                .collect(Collectors.toList());
    }

    public List<CommentResponse> getByUserId(Long userId) throws UserNotFound {
        Optional<UserEntity> optionalUser = userRepo.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new UserNotFound("User not found");
        }

        return commentRepo.findAllByUser_Id(userId).stream()
                .map(CommentMapper.INSTANCE::commentEntityToCommentResponse)
                .collect(Collectors.toList());
    }

    public List<CommentResponse> getAll() {
        List<CommentEntity> comments = new ArrayList<>();
        commentRepo.findAll().forEach(comments::add);

        return comments.stream()
                .map(CommentMapper.INSTANCE::commentEntityToCommentResponse)
                .collect(Collectors.toList());

    }

    public void changeRating(Long commentId, Long userId, String value) throws Exception {
        CommentEntity comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new Exception("Comment not found"));

        CommentRatingEntity commentRating = commentRatingRepo.findByCommentIdAndUserId(commentId, userId)
                .orElseGet(() -> {
                    CommentRatingEntity newRating = new CommentRatingEntity();
                    newRating.setComment(comment);
                    try {
                        newRating.setUser(userRepo.findById(userId)
                                .orElseThrow(() -> new Exception("User not found")));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return newRating;
                });

        Integer currentRating = commentRating.getRating();

        switch (value) {
            case "increase":
                if (currentRating == 0) {
                    comment.setRating(comment.getRating() + 1);
                    commentRating.setRating(1);
                } else if (currentRating == -1) {
                    comment.setRating(comment.getRating() + 2);
                    commentRating.setRating(1);
                }
                break;
            case "decrease":
                if (currentRating == 0) {
                    comment.setRating(comment.getRating() - 1);
                    commentRating.setRating(-1);
                } else if (currentRating == 1) {
                    comment.setRating(comment.getRating() - 2);
                    commentRating.setRating(-1);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid operation");
        }

        commentRatingRepo.save(commentRating);
        commentRepo.save(comment);
    }
}
