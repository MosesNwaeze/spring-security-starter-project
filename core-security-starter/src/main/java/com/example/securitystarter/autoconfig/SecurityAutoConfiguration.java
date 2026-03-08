package com.example.securitystarter.autoconfig;

import com.example.securitystarter.config.JwtProperties;
import com.example.securitystarter.exception.AccessDeniedException;
import com.example.securitystarter.exception.AuthenticationException;
import com.example.securitystarter.filter.JwtAuthenticationFilter;
import com.example.securitystarter.jwt.JwtService;
import com.example.securitystarter.logging.RequestLoggingFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDateTime;

@AutoConfiguration
@EnableConfigurationProperties(JwtProperties.class)
@ConditionalOnWebApplication
@EnableWebSecurity
public class SecurityAutoConfiguration {

    @Bean
    public JwtService jwtService(JwtProperties jwtProperties) {
        return new JwtService(jwtProperties);
    }

    @Bean
    public RequestLoggingFilter requestLoggingFilter() {
        return new RequestLoggingFilter();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(
        HttpSecurity http,
        JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/public/**", "/api/auth/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(authenticationException())
                .accessDeniedHandler(accessDeniedException())
            );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
        BCryptPasswordEncoder bCryptPasswordEncoder,
        UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return new ProviderManager(authProvider);
    }

    @Bean
    public AccessDeniedHandler accessDeniedException() {
        return (request, response, ex) -> {

            AccessDeniedException accessDeniedException = new AccessDeniedException(
                ex.getMessage(),
                403,
                request.getRequestURI(),
                LocalDateTime.now()
            );

            response.setStatus(accessDeniedException.getStatus());
            response.setContentType("application/json");
            response.getWriter().write("""
                {
                  "error": "Forbidden",
                  "message": "You do not have permission"
                }
                """);
        };
    }

    @Bean
    public AuthenticationEntryPoint authenticationException() {

        return (request, response, authException) -> {

            AuthenticationException exception =
                new AuthenticationException(
                    "Authentication required",
                    401,
                    request.getRequestURI(),
                    LocalDateTime.now()
                );

            response.setStatus(exception.getStatus());
            response.setContentType("application/json");
            response.getWriter().write("""
                {
                  "error": "Unauthorized",
                  "message": "Authentication is required"
                }
                """);
        };
    }
}
