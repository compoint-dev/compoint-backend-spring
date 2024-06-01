package com.example.compoint.service;

import com.example.compoint.dtos.UserSignupRequest;
import com.example.compoint.entity.RoleEntity;
import com.example.compoint.entity.UserEntity;
import com.example.compoint.entity.UserInfoEntity;
import com.example.compoint.exception.RoleNotFound;
import com.example.compoint.exception.UserAlreadyExist;
import com.example.compoint.repository.RoleRepo;
import com.example.compoint.repository.UserRepo;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    public void create(UserSignupRequest userSignupRequest) throws UserAlreadyExist, RoleNotFound {
        if (userRepo.existsByUsername(userSignupRequest.getUsername())) {
            throw new UserAlreadyExist("User already exists");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userSignupRequest.getUsername());
        userEntity.setEmail(userSignupRequest.getEmail());
        userEntity.setPassword(passwordEncoder.encode(userSignupRequest.getPassword()));

        Optional<RoleEntity> optionalRole = roleRepo.findByName("ROLE_USER");
        if (optionalRole.isEmpty()) {
            throw new RoleNotFound("Role 'USER' not found");
        }

        userEntity.getRoles().add(optionalRole.get());

        UserInfoEntity userInfo = new UserInfoEntity();
        userInfo.setUser(userEntity);
        userEntity.setUserInfo(userInfo);

        userRepo.save(userEntity);
    }

    public String logout(HttpServletResponse response) {
        ResponseCookie deleteAccessTokenCookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie deleteRefreshTokenCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteAccessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, deleteRefreshTokenCookie.toString());
        return "Logout successful";
    }
}
