package com.example.app.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    private final ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String json) {
        try {
            return  objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
