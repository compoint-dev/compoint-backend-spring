package com.example.compoint.security;

import com.example.compoint.config.JwtAuthFilter;
import com.example.compoint.repository.UserRepo;
import com.example.compoint.service.UserDetailsImplService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserRepo userRepo;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsImplService(userRepo);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeRequests()
                // Public endpoints
                .requestMatchers("/api/auth/check-auth", "/api/auth/signin", "/api/auth/refresh").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/standups/{id}", "/api/standups", "/api/standups/user/{userid}").permitAll()
                .requestMatchers("/api/comments/standup", "/api/blogs").permitAll()

                // Endpoints for anonymous users
                .requestMatchers("/api/auth/signup").access("isAnonymous()")

                // Endpoints for authenticated users
                .requestMatchers("/api/auth/verify", "/api/auth/logout").authenticated()
                .requestMatchers("/api/users/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/standups/{userid}").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/standups/{id}/watch-later").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/standups/{id}").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/standups/{id}", "/api/standups/{id}/watch-later").authenticated()
                .requestMatchers("/api/standupinfo/rating/{id}", "/api/standupinfo/{id}").authenticated()
                .requestMatchers("/api/comments/{id}/delete", "/api/comments/{standupid}/user/{userid}", "/api/comments/rating/{commentid}/user/{userId}").authenticated()
                .requestMatchers("/api/blogs/{id}", "/api/blogs/{userid}").authenticated()

                // Admin endpoints
                .requestMatchers("/api/roles/**", "/api/comments", "/api/comments/user").hasRole("ADMIN")

                // Any other requests
                .anyRequest().permitAll()

                .and()
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
