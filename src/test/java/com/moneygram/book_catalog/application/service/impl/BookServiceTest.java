package com.moneygram.book_catalog.application.service.impl;

import com.moneygram.book_catalog.domain.model.Book;
import com.moneygram.book_catalog.domain.repository.IBookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {
    private BookService service;
    private IBookRepository repository;

    @BeforeEach
    void setUp() {
        repository = mock(IBookRepository.class);
        service = new BookService(repository);
    }

    @Test
    void testFindAllBooks() {
        when(repository.findAll()).thenReturn(List.of(new Book("1", "A", "B", "X", "", true)));
        List<Book> books = service.findAll();
        assertEquals(1, books.size());
        assertEquals("A", books.getFirst().title());
    }

    @Test
    void testSaveBook() {
        Book book = new Book("1", "Título", "Autor", "1234567890", "", true);
        when(repository.create(any(Book.class))).thenReturn(Optional.of(book));
        Book saved = service.create(book);
        assertEquals("Título", saved.title());
    }

    @Test
    void testFindByIdSuccess() {
        Book book = new Book("1", "A", "B", "X", "", true);
        when(repository.findById("1")).thenReturn(Optional.of(book));
        Book found = service.findById("1");
        assertEquals("A", found.title());
    }

    @Test
    void testFindByIdNotFound() {
        when(repository.findById("2")).thenReturn(Optional.empty());
        assertThrows(com.moneygram.book_catalog.application.exception.EntityNotFoundException.class, () -> service.findById("2"));
    }

    @Test
    void testCreateBookFailure() {
        Book book = new Book("1", "Título", "Autor", "1234567890", "", true);
        when(repository.create(book)).thenReturn(Optional.empty());
        assertThrows(com.moneygram.book_catalog.application.exception.EntityCreationException.class, () -> service.create(book));
    }

    @Test
    void testUpdateBookSuccess() {
        Book book = new Book("1", "A", "B", "X", "", true);
        when(repository.update("1", book)).thenReturn(Optional.of(book));
        Book updated = service.update("1", book);
        assertEquals("A", updated.title());
    }

    @Test
    void testUpdateBookFailure() {
        Book book = new Book("1", "A", "B", "X", "", true);
        when(repository.update("1", book)).thenReturn(Optional.empty());
        assertThrows(com.moneygram.book_catalog.application.exception.EntityUpdateException.class, () -> service.update("1", book));
    }

    @Test
    void testDeleteBookSuccess() {
        Book book = new Book("1", "A", "B", "X", "", true);
        Book inactiveBook = book.copyWithActive(false);
        when(repository.findById("1")).thenReturn(Optional.of(book));
        when(repository.update("1", inactiveBook)).thenReturn(Optional.of(inactiveBook));
        Book deleted = service.delete("1");
        assertFalse(deleted.active());
    }

    @Test
    void testDeleteBookNotFound() {
        when(repository.findById("2")).thenReturn(Optional.empty());
        assertThrows(com.moneygram.book_catalog.application.exception.EntityNotFoundException.class, () -> service.delete("2"));
    }

    @Test
    void testDeleteBookUpdateFailure() {
        Book book = new Book("1", "A", "B", "X", "", true);
        Book inactiveBook = book.copyWithActive(false);
        when(repository.findById("1")).thenReturn(Optional.of(book));
        when(repository.update("1", inactiveBook)).thenReturn(Optional.empty());
        assertThrows(com.moneygram.book_catalog.application.exception.EntityUpdateException.class, () -> service.delete("1"));
    }

    @Test
    void testFindByTemplate() {
        Book template = new Book(null, "A", null, null, null, null);
        Book book = new Book("1", "A", "B", "X", "", true);
        when(repository.findByTemplate(template)).thenReturn(List.of(book));
        List<Book> result = service.findByTemplate(template);
        assertEquals(1, result.size());
        assertEquals("A", result.getFirst().title());
    }
}
