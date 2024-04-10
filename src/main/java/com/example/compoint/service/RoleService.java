package com.example.compoint.service;

import com.example.compoint.entity.RoleEntity;
import com.example.compoint.entity.UserEntity;
import com.example.compoint.exception.RoleAlreadyExist;
import com.example.compoint.repository.RoleRepo;
import com.example.compoint.repository.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepo roleRepo;
    private final UserRepo userRepo;

    public void create(RoleEntity role) throws RoleAlreadyExist {
        Optional<RoleEntity> optionalRole = roleRepo.findByName(role.getName());
        if (optionalRole.isPresent()) {
            throw new RoleAlreadyExist("Role already exists");
        }
        roleRepo.save(role);
    }

    public List<RoleEntity> getAll() {
        List<RoleEntity> roles = new ArrayList<>();
        roleRepo.findAll().forEach(roles::add);
        return roles;
    }

    public UserEntity assignRoleToUser(Long userId, RoleEntity role) throws EntityNotFoundException {
        Optional<UserEntity> optionalUser = userRepo.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new EntityNotFoundException("User not found with id: " + userId);
        }
        UserEntity user = optionalUser.get();

        Optional<RoleEntity> optionalRole = roleRepo.findByName(role.getName());
        if (!optionalRole.isPresent()) {
            throw new EntityNotFoundException("Role not found with name: " + role.getName());
        }
        RoleEntity newRole = optionalRole.get();

        if (!user.getRoles().contains(newRole)) {
            user.getRoles().add(newRole);
            userRepo.save(user);
        }

        return user;
    }
}
