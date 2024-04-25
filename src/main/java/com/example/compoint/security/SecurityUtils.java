package com.example.compoint.security;

import com.example.compoint.config.UserDetailsImpl;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class SecurityUtils {
    public static boolean hasPermission(Long userId, UserDetailsImpl userDetails) {
        return userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")) || userId.equals(userDetails.getId());
    }
}
