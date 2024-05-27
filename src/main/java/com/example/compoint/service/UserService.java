package com.example.compoint.service;

import com.example.compoint.config.UserDetailsImpl;
import com.example.compoint.dtos.UserDTO;
import com.example.compoint.entity.RoleEntity;
import com.example.compoint.entity.StandupEntity;
import com.example.compoint.entity.UserEntity;
import com.example.compoint.entity.UserInfoEntity;
import com.example.compoint.exception.RoleNotFound;
import com.example.compoint.exception.UserAlreadyExist;
import com.example.compoint.exception.UserNotAuthorized;
import com.example.compoint.exception.UserNotFound;
import com.example.compoint.mappers.UserMapper;
import com.example.compoint.repository.RoleRepo;
import com.example.compoint.repository.StandupRepo;
import com.example.compoint.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final StandupRepo standupRepo;
    private final PasswordEncoder passwordEncoder;

    public String create(UserEntity user) throws UserAlreadyExist, RoleNotFound {

        Optional<UserEntity> optionalUser = userRepo.findByUsername(user.getUsername());
        if (optionalUser.isPresent()) {
            throw new UserAlreadyExist("User already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Optional<RoleEntity> optionalRole = roleRepo.findByName("USER");
        if (optionalRole.isEmpty()) {
            throw new RoleNotFound("Role 'USER' not found");
        }

        user.getRoles().add(optionalRole.get());

        UserInfoEntity userInfo = new UserInfoEntity();
        userInfo.setUser(user);
        user.setUserInfo(userInfo);
        userRepo.save(user);
        return "User created";
    }

    public List<UserDTO> getAll() {
        List<UserEntity> users = new ArrayList<>();
        userRepo.findAll().forEach(users::add);

        return users.stream()
                .map(UserMapper.INSTANCE::userEntityToUserDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> getById(Long id) throws UserNotFound {
        Optional<UserEntity> optionalUser = userRepo.findById(id);
        if (optionalUser.isPresent()) {
            return Optional.ofNullable(UserMapper.INSTANCE.userEntityToUserDTO(optionalUser.get()));
        } else {
            throw new UserNotFound("User not found");
        }
    }

    public Optional<UserDTO> getByUsername(String username) throws UserNotFound {
        Optional<UserEntity> optionalUser = userRepo.findByUsername(username);
        if (optionalUser.isPresent()) {
            return Optional.ofNullable(UserMapper.INSTANCE.userEntityToUserDTO(optionalUser.get()));
        } else {
            throw new UserNotFound("User not found");
        }
    }

    //TODO: Доделать
    public UserEntity update(Long id, UserEntity user) throws UserNotFound, UserAlreadyExist {
        Optional<UserEntity> optionalUser = userRepo.findById(id);
        if (!optionalUser.isPresent()) {
            throw new UserNotFound("User not found");
        }

        UserEntity existingUser = optionalUser.get();

        Optional<UserEntity> optionalNewInfo = userRepo.findByUsername(user.getUsername());
        if (optionalNewInfo.isPresent() && !optionalNewInfo.get().getId().equals(existingUser.getId())) {
            throw new UserAlreadyExist("Username already taken");
        }

        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(existingUser);
    }

    public String delete(Long id) throws UserNotFound {
        Optional<UserEntity> optionalUser = userRepo.findById(id);
        if (!optionalUser.isPresent()) {
            throw new UserNotFound("User not found");
        }

        UserEntity user = optionalUser.get();

        List<StandupEntity> standups = standupRepo.findByUserId(user.getId());
        for (StandupEntity standup : standups) {
            standupRepo.deleteById(standup.getId());
        }

        userRepo.deleteById(id);
        return "User with ID " + id + " has been deleted successfully";
    }

    public Map<String, Long> getCurrentUser(Authentication authentication) throws UserNotAuthorized {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotAuthorized("User not authorized");
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return Collections.singletonMap("userId", userDetails.getId());
    }
}
