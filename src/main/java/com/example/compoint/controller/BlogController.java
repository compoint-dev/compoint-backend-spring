package com.example.compoint.controller;

import com.example.compoint.entity.BlogEntity;
import com.example.compoint.entity.UserEntity;
import com.example.compoint.exception.BlogAlreadyExist;
import com.example.compoint.exception.RoleNotFound;
import com.example.compoint.exception.UserAlreadyExist;
import com.example.compoint.exception.UserNotFound;
import com.example.compoint.service.BlogService;
import com.example.compoint.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    //TODO:Доделать выброс ошибок
    @Operation(summary = "Create a new user", description = "Creates a new blog and returns it")
    @ApiResponse(responseCode = "200", description = "Blog created successfully")
    @ApiResponse(responseCode = "409", description = "Blog already exists")
    @ApiResponse(responseCode = "404", description = "Role not found")
    @PostMapping("/{userid}")
    public ResponseEntity<?> createBlog(@PathVariable Long userid, @RequestBody BlogEntity blogEntity) {
        try {
            return ResponseEntity.ok(blogService.create(userid, blogEntity));
        } catch (UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Get all blogs", description = "Get all blogs and returns it")
    @ApiResponse(responseCode = "200", description = "List of all blogs retrieved successfully")
    @GetMapping
    public ResponseEntity<?> getAllBlogs() {
        return ResponseEntity.ok(blogService.getAll());
    }
}
