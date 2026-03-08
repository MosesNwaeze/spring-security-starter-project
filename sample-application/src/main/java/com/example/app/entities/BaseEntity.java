package com.example.app.entities;

import com.example.app.entities.listener.BaseEntityListener;
import com.example.securitystarter.model.SecureUser;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
@EntityListeners(BaseEntityListener.class)
public abstract class BaseEntity implements SecureUser {

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}
