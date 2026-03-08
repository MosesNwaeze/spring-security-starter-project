package com.example.app.repository;

import com.example.app.entities.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {

    Optional<ApplicationUser> findByUsername(String userName);

    boolean existsByUsername(String userName);
}
