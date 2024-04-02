package com.example.compoint.service;

import com.example.compoint.entity.UserEntity;
import com.example.compoint.exception.UserAlreadyExist;
import com.example.compoint.exception.UserNotFound;
import com.example.compoint.repository.RoleRepo;
import com.example.compoint.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private final UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public Optional<UserEntity> getById(Long id) throws UserNotFound {
        Optional<UserEntity> user = userRepo.findById(id);
        if (user.isPresent()) {
            return user;
        } else {
            throw new UserNotFound("User does not exist");
        }
    }
    public Optional<UserEntity> getByUsername(String username) throws UserNotFound {
        Optional<UserEntity> user = userRepo.findByUsername(username);
        if (user.isPresent()) {
            return user;
        } else {
            throw new UserNotFound("User does not exist");
        }
    }

    public List<UserEntity> getAll () {
        List<UserEntity> users = new ArrayList<>();
        userRepo.findAll().forEach(users::add);
        return users;
    }
}
