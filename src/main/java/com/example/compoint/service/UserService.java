package com.example.compoint.service;

import com.example.compoint.config.UserDetailsImpl;
import com.example.compoint.dtos.userDTO.UserSignupRequest;
import com.example.compoint.dtos.userDTO.UserUpdateRequest;
import com.example.compoint.dtos.userDTO.UserUpdateResponse;
import com.example.compoint.dtos.userDTO.UserWithoutPasswordDTO;
import com.example.compoint.entity.RoleEntity;
import com.example.compoint.entity.StandupEntity;
import com.example.compoint.entity.UserEntity;
import com.example.compoint.entity.UserInfoEntity;
import com.example.compoint.exception.RoleNotFound;
import com.example.compoint.exception.UserAlreadyExist;
import com.example.compoint.exception.UserNotAuthorized;
import com.example.compoint.exception.UserNotFound;
import com.example.compoint.mappers.UserInfoMapper;
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

    public String create(UserSignupRequest userSignupRequest) throws UserAlreadyExist, RoleNotFound {
        if (userRepo.existsByUsername(userSignupRequest.getUsername())) {
            throw new UserAlreadyExist("User already exists");
        }

        UserEntity userEntity = UserMapper.INSTANCE.userSignupRequestToUserEntity(userSignupRequest);
        userEntity.setPassword(passwordEncoder.encode(userSignupRequest.getPassword()));

        RoleEntity role = roleRepo.findByName("ROLE_USER")
                .orElseThrow(() -> new RoleNotFound("Role 'USER' not found"));

        userEntity.getRoles().add(role);

        UserInfoEntity userInfo = new UserInfoEntity();
        userInfo.setUser(userEntity);
        userEntity.setUserInfo(userInfo);

        userRepo.save(userEntity);
        return "User created";
    }

    public List<UserWithoutPasswordDTO> getAll() {
        List<UserEntity> users = new ArrayList<>();
        userRepo.findAll().forEach(users::add);

        return users.stream()
                .map(UserMapper.INSTANCE::userEntityToUserWithoutPasswordDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserWithoutPasswordDTO> getById(Long id) throws UserNotFound {
        Optional<UserEntity> optionalUser = userRepo.findById(id);
        if (optionalUser.isPresent()) {
            return Optional.ofNullable(UserMapper.INSTANCE.userEntityToUserWithoutPasswordDTO(optionalUser.get()));
        } else {
            throw new UserNotFound("User not found");
        }
    }

    public Optional<UserWithoutPasswordDTO> getByUsername(String username) throws UserNotFound {
        Optional<UserEntity> optionalUser = userRepo.findByUsername(username);
        if (optionalUser.isPresent()) {
            return Optional.ofNullable(UserMapper.INSTANCE.userEntityToUserWithoutPasswordDTO(optionalUser.get()));
        } else {
            throw new UserNotFound("User not found");
        }
    }

    //TODO: Доделать
    public UserUpdateResponse update(Long id, UserUpdateRequest user) throws UserNotFound, UserAlreadyExist {
        UserEntity existingUser = userRepo.findById(id).orElseThrow(() -> new UserNotFound("User not found"));

        Optional<UserEntity> optionalNewInfo = userRepo.findByUsername(user.getUsername());
        if (optionalNewInfo.isPresent() && !optionalNewInfo.get().getId().equals(existingUser.getId())) {
            throw new UserAlreadyExist("Username already taken");
        }

        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());

        if (existingUser.getUserInfo() != null) {
            UserInfoMapper.INSTANCE.updateUserInfoFromDTO(user.getUserInfo(), existingUser.getUserInfo());
        } else {
            existingUser.setUserInfo(UserInfoMapper.INSTANCE.userInfoDTOToUserInfoEntity(user.getUserInfo()));
            existingUser.getUserInfo().setUser(existingUser);
        }

        userRepo.save(existingUser);
        return UserMapper.INSTANCE.userEntityToUserUpdateResponse(existingUser);
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
