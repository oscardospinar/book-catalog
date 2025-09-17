package com.moneygram.book_catalog.presentation.dto;

import lombok.Builder;

@Builder
public record BookResponseDto (String id, String title, String author, String isbn, String description, Boolean active) {
}
