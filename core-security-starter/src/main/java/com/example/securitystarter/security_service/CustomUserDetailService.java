package com.example.securitystarter.security_service;

import com.example.securitystarter.model.SecureUser;
import com.example.securitystarter.model.SecureUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final SecureUserService secureUserService;

    public CustomUserDetailService(SecureUserService secureUserService) {
        this.secureUserService = secureUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<SecureUser> user = secureUserService.findByUserName(username);
        if (user.isEmpty()) {
            System.out.println("User not available");
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(user.get());
    }
}
