package com.moneygram.book_catalog.presentation.adapter;

import com.moneygram.book_catalog.domain.model.Book;
import com.moneygram.book_catalog.presentation.dto.BookRequestDto;
import com.moneygram.book_catalog.presentation.dto.BookResponseDto;

public class BookAdapter {
    public static Book toModel(BookRequestDto request) {
        return Book.builder()
                .title(request.title())
                .author(request.author())
                .isbn(request.isbn())
                .description(request.description())
                .active(true)
                .build();
    }

    public static BookResponseDto toDto(Book model) {
        return BookResponseDto.builder()
                .id(model.id())
                .title(model.title())
                .author(model.author())
                .isbn(model.isbn())
                .description(model.description())
                .active(model.active())
                .build();
    }
}
