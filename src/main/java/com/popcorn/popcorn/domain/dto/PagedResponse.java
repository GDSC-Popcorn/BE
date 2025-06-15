package com.popcorn.popcorn.domain.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record PagedResponse<T>(
        List<T> content,
        int currentPage,
        int totalPages
) {
    public static <T> PagedResponse<T> from(Page<T> page) {
        return new PagedResponse<>(
                page.getContent(),
                page.getNumber() + 1, // 0부터 시작하므로 +1
                page.getTotalPages()
        );
    }
}

