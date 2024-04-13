package com.example.compoint.service;

import com.example.compoint.config.UserDetailsImpl;
import com.example.compoint.entity.UserEntity;
import com.example.compoint.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userOptional = Optional.ofNullable(userRepo.findByUsername(username));
        if (userOptional.isPresent()) {
            return new UserDetailsImpl(userOptional.get());
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}
