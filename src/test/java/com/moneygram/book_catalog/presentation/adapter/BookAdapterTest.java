package com.moneygram.book_catalog.presentation.adapter;

import com.moneygram.book_catalog.domain.model.Book;
import com.moneygram.book_catalog.presentation.dto.BookRequestDto;
import com.moneygram.book_catalog.presentation.dto.BookResponseDto;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookAdapterTest {
    @Test
    void testToModel() {
        BookRequestDto dto = new BookRequestDto("Título", "Autor", "1234567890", "");
        Book book = BookAdapter.toModel(dto);
        assertEquals("Título", book.title());
        assertEquals("Autor", book.author());
        assertEquals("1234567890", book.isbn());
        assertTrue(book.active());
    }

    @Test
    void testToDto() {
        Book book = new Book("1", "Título", "Autor", "1234567890", "", true);
        BookResponseDto dto = BookAdapter.toDto(book);
        assertEquals("1", dto.id());
        assertEquals("Título", dto.title());
        assertEquals("Autor", dto.author());
        assertEquals("1234567890", dto.isbn());
        assertTrue(dto.active());
    }
}

