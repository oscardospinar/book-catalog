package com.moneygram.book_catalog.domain.model;

import lombok.Builder;

@Builder
public record Book(String id, String title, String author, String isbn, String description, Boolean active) {
    public Book copyWithActive(boolean active) {
        return builder()
                .id(id)
                .title(title)
                .author(author)
                .isbn(isbn)
                .description(description)
                .active(active)
                .build();
    }

    public Book copyWithId(String id) {
        return builder()
                .id(id)
                .title(title)
                .author(author)
                .isbn(isbn)
                .description(description)
                .active(active)
                .build();
    }
}
