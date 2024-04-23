package com.example.compoint.service;

import com.example.compoint.config.UserDetailsImpl;
import com.example.compoint.entity.UserEntity;
import com.example.compoint.exception.UserNotFound;
import com.example.compoint.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    public List<UserEntity> getAll() {
        List<UserEntity> users = new ArrayList<>();
        userRepo.findAll().forEach(users::add);
        return users;
    }

    public Optional<UserEntity> getById(Long id) throws UserNotFound {
        Optional<UserEntity> optionalUser = userRepo.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser;
        } else {
            throw new UserNotFound("User not found");
        }
    }

    public Optional<UserEntity> getByUsername(String username, Principal principal) throws UserNotFound {
        UserDetailsImpl userDetails = (UserDetailsImpl) ((Authentication) principal).getPrincipal();

        if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")) || userDetails.getUsername().equals(username)) {
            Optional<UserEntity> user = Optional.ofNullable(userRepo.findByUsername(username));
            if (!user.isPresent()) {
                throw new UserNotFound("User not found");
            }
            return user;
        } else {
            throw new UserNotFound("Access denied");
        }
    }

    public Optional<UserDetails> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return Optional.of(userDetails);
        }
        return Optional.empty();
    }
}
