package com.example.compoint.mappers;

import com.example.compoint.dtos.CommentResponse;
import com.example.compoint.dtos.CommentRequest;
import com.example.compoint.entity.CommentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "standup", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "rating", ignore = true)
    CommentEntity commentRequestToCommentEntity(CommentRequest dto);

    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "standup.name", target = "standupName")
    CommentResponse commentEntityToCommentResponse(CommentEntity entity);
}
