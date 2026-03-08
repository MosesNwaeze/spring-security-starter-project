package com.example.app.entities.listener;

import com.example.app.entities.BaseEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

public class BaseEntityListener {
    @PrePersist
    public void prePersist(BaseEntity entity) {
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
    }

    @PreUpdate
    public void preUpdate(BaseEntity entity) {
        entity.setUpdatedAt(LocalDateTime.now());
    }
}
