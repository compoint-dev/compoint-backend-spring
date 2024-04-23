package com.example.compoint.dtos;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

@Getter
@Setter
public class UserDataDTO {

    private String username;
    private Collection<? extends GrantedAuthority> authorities;
    private Boolean isValid;

    public UserDataDTO(String username, Collection<? extends GrantedAuthority> authorities, Boolean isValid) {
        this.username = username;
        this.authorities = authorities;
        this.isValid = isValid;
    }
}
