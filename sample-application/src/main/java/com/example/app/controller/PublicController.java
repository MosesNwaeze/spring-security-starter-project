package com.example.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resources;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicController {

  private final ObjectMapper objectMapper;
  @GetMapping("/health")
  public ResponseEntity<String> health() throws JsonProcessingException {

    String value = objectMapper.writeValueAsString("{status: up}");

    return ResponseEntity.status(HttpStatus.OK)
      .body(value);
  }
}
