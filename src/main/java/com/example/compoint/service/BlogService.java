package com.example.compoint.service;

import com.example.compoint.dtos.BlogDTO;
import com.example.compoint.entity.BlogEntity;
import com.example.compoint.entity.UserEntity;
import com.example.compoint.exception.UserNotFound;
import com.example.compoint.mappers.BlogMapper;
import com.example.compoint.mappers.StandupMapper;
import com.example.compoint.repository.BlogRepo;
import com.example.compoint.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlogService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BlogRepo blogRepo;

    public BlogDTO create(Long userid, BlogEntity blog) throws UserNotFound {
        Optional<UserEntity> userOptional = userRepo.findById(userid);
        UserEntity user = userOptional.orElseThrow(() -> new UserNotFound("User not found"));

        blog.setUser(user);

        BlogEntity savedBlog = blogRepo.save(blog);

        return BlogMapper.INSTANCE.blogEntityToBlogDTO(savedBlog);
    }
}
