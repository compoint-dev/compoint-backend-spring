package com.example.compoint.config;

import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

public class UserData  {
    private String username;
    private Collection<? extends GrantedAuthority> authorities;
    private Boolean isValid;

    public UserData(String username, Collection<? extends GrantedAuthority> authorities, Boolean isValid) {
        this.username = username;
        this.authorities = authorities;
        this.isValid = isValid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }
}
