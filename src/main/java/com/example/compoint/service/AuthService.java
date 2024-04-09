package com.example.compoint.service;

import com.example.compoint.entity.RoleEntity;
import com.example.compoint.entity.UserEntity;
import com.example.compoint.exception.RoleNotFound;
import com.example.compoint.exception.UserAlreadyExist;
import com.example.compoint.repository.RoleRepo;
import com.example.compoint.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepo userRepo, RoleRepo roleRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }


    public void create(UserEntity user) throws UserAlreadyExist, RoleNotFound {
        //Поиск дубликата
        Optional<UserEntity> existingUser = userRepo.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExist("User already exists");
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            Optional<RoleEntity> userRole = roleRepo.findByName("USER");
            if (userRole == null) {
                // В случае отсутствия роли USER, обработать ошибку или вернуть null
                throw new RoleNotFound("Role 'USER' not found");
            }
            user.getRoles().add(userRole.get());

            userRepo.save(user);
        }
    }
}
