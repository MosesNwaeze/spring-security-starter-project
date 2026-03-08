package com.example.app.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaginatedResponse<T> {
    private List<T> data;
    private int totalPages;
    private long totalElements;
    private int page;
    private int size;
    private boolean isFirst;
    private boolean isLast;
}
