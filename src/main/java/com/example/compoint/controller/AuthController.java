package com.example.compoint.controller;

import com.example.compoint.dtos.AuthRequestDTO;
import com.example.compoint.dtos.JwtResponseDTO;
import com.example.compoint.dtos.UserDTO;
import com.example.compoint.entity.UserEntity;
import com.example.compoint.exception.RoleNotFound;
import com.example.compoint.exception.UserAlreadyExist;
import com.example.compoint.exception.UserNotFound;
import com.example.compoint.service.AuthService;
import com.example.compoint.service.JwtService;
import com.example.compoint.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Value("${compoint.cookieExpiry}")
    private int cookieExpiry;

    @Operation(summary = "Sign in", description = "Authenticates a user and returns a JWT token.")
    @ApiResponse(responseCode = "200", description = "Authentication successful")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    @PostMapping("/signin")
    public JwtResponseDTO authenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO, HttpServletResponse response) throws UserNotFound {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
        if (authentication.isAuthenticated()) {
            Optional<UserDTO> user = userService.getByUsername(authRequestDTO.getUsername());
            String accessToken = jwtService.GenerateAccessToken(user.get().getId(), authRequestDTO.getUsername());
            String refreshToken = jwtService.GenerateRefreshToken(user.get().getId(), authRequestDTO.getUsername());

            ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", accessToken)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(cookieExpiry)
                    .build();

            ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(604800) // Длительный срок жизни для рефреш токена
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

            return JwtResponseDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .id(user.get().getId())
                    .build();
        } else {
            throw new UsernameNotFoundException("Invalid user request.");
        }
    }

    @Operation(summary = "Sign up", description = "Registers a new user.")
    @ApiResponse(responseCode = "200", description = "User created successfully")
    @ApiResponse(responseCode = "409", description = "User already exists")
    @ApiResponse(responseCode = "404", description = "Role not found")
    @PostMapping("/signup")
    public ResponseEntity<?> createNewUser(@RequestBody UserEntity user) {
        try {
            authService.create(user);
            return ResponseEntity.ok("User created");
        } catch (UserAlreadyExist e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RoleNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Verify token", description = "Verifies the user's token and returns user information if valid.")
    @ApiResponse(responseCode = "200", description = "Token valid")
    @ApiResponse(responseCode = "401", description = "Token invalid or expired")
    @GetMapping("/verify")
    public ResponseEntity<?> verifyToken() throws UserNotFound {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            Optional<UserDTO> user = userService.getByUsername(authentication.getName());
            if (user.isPresent()) {
                JwtResponseDTO jwtResponseDTO = JwtResponseDTO.builder()
                        .accessToken(jwtService.GenerateAccessToken(user.get().getId(), user.get().getUsername()))
                        .id(user.get().getId())
                        .build();
                return ResponseEntity.ok(jwtResponseDTO);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalid or expired");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@CookieValue("refreshToken") String refreshToken) {
        try {
            if (jwtService.validateToken(refreshToken)) {
                String username = jwtService.extractUsername(refreshToken);
                Long userId = Long.parseLong(jwtService.extractClaim(refreshToken, claims -> claims.get("userId").toString()));
                String newAccessToken = jwtService.GenerateAccessToken(userId, username);
                return ResponseEntity.ok(new JwtResponseDTO(newAccessToken, null, userId));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }
}
