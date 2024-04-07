package com.example.compoint.service;

import com.example.compoint.entity.RoleEntity;
import com.example.compoint.entity.UserEntity;
import com.example.compoint.exception.UserAlreadyExist;
import com.example.compoint.repository.RoleRepo;
import com.example.compoint.repository.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private final RoleRepo roleRepo;
    private final UserRepo userRepo;
    public RoleService(RoleRepo roleRepo, UserRepo userRepo) {
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
    }

    public void create(RoleEntity role) {
        roleRepo.save(role);
    }

    public List<RoleEntity> getAll () {
        List<RoleEntity> roles = new ArrayList<>();
        roleRepo.findAll().forEach(roles::add);
        return roles;
    }

    public UserEntity assignRoleToUser(Long userId, RoleEntity role) {
        Optional<UserEntity> userOptional = userRepo.findById(userId);
        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException("User not found with id: " + userId);
        }

        UserEntity user = userOptional.get();
        user.getRoles().add(role);
        userRepo.save(user);
        return user;
    }
}
