package com.example.compoint.service;

import com.example.compoint.entity.UserEntity;
import com.example.compoint.exception.UserAlreadyExist;
import com.example.compoint.exception.UserNotFound;
import com.example.compoint.repository.RoleRepo;
import com.example.compoint.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public Optional<UserEntity> getByUsername(String username) throws UserNotFound {
        Optional<UserEntity> optionalUser = Optional.ofNullable(userRepo.findByUsername(username));
        if (optionalUser.isPresent()) {
            return optionalUser;
        } else {
            throw new UserNotFound("User not found");
        }
    }
}
