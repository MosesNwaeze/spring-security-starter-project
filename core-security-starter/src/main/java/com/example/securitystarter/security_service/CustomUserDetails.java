package com.example.securitystarter.security_service;

import com.example.securitystarter.model.SecureUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final SecureUser user;

    public CustomUserDetails(SecureUser user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new java.util.ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    public SecureUser getUser() {
        return user;
    }

}
