package com.example.compoint.service;

import com.example.compoint.dtos.RoleResponse;
import com.example.compoint.dtos.RoleRequest;
import com.example.compoint.entity.RoleEntity;
import com.example.compoint.entity.UserEntity;
import com.example.compoint.exception.RoleAlreadyExist;
import com.example.compoint.mappers.RoleMapper;
import com.example.compoint.repository.RoleRepo;
import com.example.compoint.repository.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepo roleRepo;
    private final UserRepo userRepo;

    public void create(RoleRequest role) throws RoleAlreadyExist {
        Optional<RoleEntity> optionalRole = roleRepo.findByName(role.getName());
        if (optionalRole.isPresent()) {
            throw new RoleAlreadyExist("Role already exists");
        }
        roleRepo.save(RoleMapper.INSTANCE.roleRequestToRoleEntity(role));
    }

    public List<RoleResponse> getAll() {
        List<RoleEntity> roles = new ArrayList<>();
        roleRepo.findAll().forEach(roles::add);

        return roles.stream()
                .map(RoleMapper.INSTANCE::roleEntityToRoleResponse)
                .collect(Collectors.toList());
    }

    public String assignRoleToUser(Long userId, RoleRequest role) throws EntityNotFoundException {
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

        return "Role assigned";
    }
}
