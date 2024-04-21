package com.example.compoint.controller;

import com.example.compoint.config.UserData;
import com.example.compoint.dtos.AuthRequestDTO;
import com.example.compoint.dtos.JwtResponseDTO;
import com.example.compoint.dtos.RefreshTokenRequestDTO;
import com.example.compoint.entity.RefreshTokenEntity;
import com.example.compoint.entity.UserEntity;
import com.example.compoint.exception.RoleNotFound;
import com.example.compoint.exception.UserAlreadyExist;
import com.example.compoint.service.AuthService;
import com.example.compoint.service.JwtService;
import com.example.compoint.service.RefreshTokenService;
import com.example.compoint.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private  final UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${compoint.cookieExpiry}")
    private int cookieExpiry;

    @PostMapping("/signin")
    public JwtResponseDTO AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO, HttpServletResponse response){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
        if(authentication.isAuthenticated()){
            RefreshTokenEntity refreshToken = refreshTokenService.createOrUpdateRefreshToken(authRequestDTO.getUsername());
            String  accessToken = jwtService.GenerateToken(authRequestDTO.getUsername());
            ResponseCookie cookie = ResponseCookie.from("accessToken", accessToken)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(cookieExpiry)
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            return JwtResponseDTO.builder()
                    .accessToken(accessToken)
                    .token(refreshToken.getToken()).build();

        } else {
            throw new UsernameNotFoundException("invalid user request..!!");
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> ValidateSession() {
        Optional<UserDetails> userDetailsOpt = userService.getCurrentUser();
        if (userDetailsOpt.isPresent()) {
            UserDetails userDetails = userDetailsOpt.get();
            return ResponseEntity.ok(new UserData(userDetails.getUsername(), userDetails.getAuthorities(), userDetails.isCredentialsNonExpired()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/signup")
    public ResponseEntity createNewUser(@RequestBody UserEntity user) {
        try {
            authService.create(user);
            return ResponseEntity.ok("User created");
        } catch (UserAlreadyExist | RoleNotFound e) {
            return handleException(e);
        }
    }

    @PostMapping("/refreshToken")
    public JwtResponseDTO refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO){
        return refreshTokenService.findByToken(refreshTokenRequestDTO.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshTokenEntity::getUserEntity)
                .map(userInfo -> {
                    String accessToken = jwtService.GenerateToken(userInfo.getUsername());
                    return JwtResponseDTO.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequestDTO.getToken()).build();
                }).orElseThrow(() ->new RuntimeException("Refresh Token is not in DB..!!"));
    }
    private ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.badRequest().body("Error: " + e.getMessage());
    }
}
