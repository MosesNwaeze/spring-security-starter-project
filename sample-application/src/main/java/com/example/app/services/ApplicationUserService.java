package com.example.app.services;

import com.example.app.dtos.ApplicationUserDto;
import com.example.app.dtos.AuthResponseDto;
import com.example.app.dtos.PaginatedResponse;
import com.example.app.entities.ApplicationUser;
import com.example.app.repository.ApplicationUserRepository;
import com.example.securitystarter.exception.AccessDeniedException;
import com.example.securitystarter.exception.AuthenticationException;
import com.example.securitystarter.exception.DuplicateException;
import com.example.securitystarter.jwt.JwtService;
import com.example.securitystarter.model.SecureUser;
import com.example.securitystarter.model.SecureUserService;
import com.example.securitystarter.security_service.CustomUserDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@Data
@Slf4j
public class ApplicationUserService implements SecureUserService {

    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public ApplicationUserDto registerUser(ApplicationUserDto user) {
        try {
            if (applicationUserRepository.existsByUsername(user.getUsername())) {
                throw new DuplicateException(
                    "User already exist with the username",
                    409,
                    "/api/auth/register",
                    LocalDateTime.now()
                );
            }
            ApplicationUser applicationUser = toEntity(user);
            ApplicationUser savedUser = applicationUserRepository.save(applicationUser);
            return toDto(savedUser);
        } catch (DuplicateException e) {
            if (e.getStatus() == 409) {
                throw e;
            } else {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    @Transactional
    public AuthResponseDto login(ApplicationUserDto user, AuthenticationManager authenticationManager) {
        try {

            Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

            if (authentication.isAuthenticated()) {
                CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
                String token = jwtService.generateToken(customUserDetails.getUser());
                String refreshToken = UUID.randomUUID().toString();

                return AuthResponseDto.builder()
                    .token(token)
                    .refreshToken(refreshToken)
                    .build();
            }

            throw new AuthenticationException("Invalid credentials", 401, "/api/auth/login", LocalDateTime.now());

        } catch (Exception e) {
            throw new AuthenticationException(e.getMessage(), 401, "/api/auth/login", LocalDateTime.now());
        }

    }

    public ApplicationUserDto getUser() {
        try {
            CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication().getPrincipal();

            return toDto((ApplicationUser) customUserDetails.getUser());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public PaginatedResponse<ApplicationUserDto> getAllUsers(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

            Page<ApplicationUser> usersPage = applicationUserRepository.findAll(pageable);

            List<ApplicationUserDto> applicationUserDtos = usersPage.getContent().stream().map(this::toDto).toList();

            return PaginatedResponse.<ApplicationUserDto>builder()
                .data(applicationUserDtos)
                .totalPages(usersPage.getTotalPages())
                .totalElements(usersPage.getTotalElements())
                .page(page)
                .isFirst(usersPage.isFirst())
                .isLast(usersPage.isLast())
                .size(size)
                .build();
        } catch (Exception e) {
            throw new AccessDeniedException(e.getMessage(), 403, "/api/admin/users", LocalDateTime.now());
        }
    }

    @Override
    public Optional<SecureUser> findByUserName(String userName) {
        return applicationUserRepository
            .findByUsername(userName)
            .map(user -> (SecureUser) user);
    }

    private ApplicationUserDto toDto(ApplicationUser user) {
        ApplicationUserDto applicationUserDto = new ApplicationUserDto();
        applicationUserDto.setUserId(user.getUserId());
        applicationUserDto.setRoles(user.getRoles());
        applicationUserDto.setUsername(user.getUsername());
        applicationUserDto.setCreatedAt(user.getCreatedAt());
        applicationUserDto.setUpdatedAt(user.getUpdatedAt());
        return applicationUserDto;
    }

    private ApplicationUser toEntity(ApplicationUserDto user) {
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setRoles(user.getRoles());
        applicationUser.setUsername(user.getUsername());
        applicationUser.setPassword(passwordEncoder.encode(user.getPassword()));
        return applicationUser;
    }

    @Transactional
    public AuthResponseDto refreshToken(AuthResponseDto authResponseDto, UserDetailsService userDetailsService) {

        boolean isTokenExpired = jwtService.isTokenExpired(authResponseDto.getToken());

        if (isTokenExpired) {
            String username = jwtService.extractUserName(authResponseDto.getToken());

            CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);

            String token = jwtService.generateToken(userDetails.getUser());

            authResponseDto.setRefreshToken(authResponseDto.getRefreshToken());
            authResponseDto.setToken(token);

            log.info("Refreshing token for user: {}, token: {}", username, token);

        }

        return authResponseDto;
    }
}
