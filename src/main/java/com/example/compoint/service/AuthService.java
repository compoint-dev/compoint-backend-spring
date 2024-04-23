package com.example.compoint.service;

import com.example.compoint.entity.RoleEntity;
import com.example.compoint.entity.UserEntity;
import com.example.compoint.exception.RoleNotFound;
import com.example.compoint.exception.UserAlreadyExist;
import com.example.compoint.repository.RoleRepo;
import com.example.compoint.repository.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    public void create(UserEntity user) throws UserAlreadyExist, RoleNotFound {
        Optional<UserEntity> optionalUser = userRepo.findByUsername(user.getUsername());
        if (optionalUser.isPresent()) {
            throw new UserAlreadyExist("User already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Optional<RoleEntity> optionalRole = roleRepo.findByName("USER");
        if (!optionalRole.isPresent()) {
            throw new RoleNotFound("Role 'USER' not found");
        }

        user.getRoles().add(optionalRole.get());

        userRepo.save(user);
    }


}
