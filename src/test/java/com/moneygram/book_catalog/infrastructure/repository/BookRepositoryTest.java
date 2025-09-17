package com.moneygram.book_catalog.infrastructure.repository;

import com.moneygram.book_catalog.domain.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookRepositoryTest {
    private BookRepository repository;

    @BeforeEach
    void setUp() {
        repository = new BookRepository();
    }

    @Test
    void testSaveAndFindByIdAndActive() {
        Book book = new Book("1", "Título", "Autor", "1234567890", "", true);
        repository.create(book);
        Book found = repository.findByIdAndActive("1").orElseThrow(RuntimeException::new);
        assertNotNull(found);
        assertEquals("Título", found.title());
    }

    @Test
    void testFindAllActive() {
        repository.create(new Book("1", "A", "B", "X", "", true));
        repository.create(new Book("2", "C", "D", "Y", "", true));
        List<Book> books = repository.findAllActive();
        assertEquals(2, books.size());
    }

    @Test
    void testUpdateBook() {
        Book book = new Book("1", "Título", "Autor", "1234567890", "desc", true);
        repository.create(book);
        Book updated = new Book("1", "Nuevo Título", "Autor", "1234567890", "desc", true);
        repository.update("1", updated);
        Book found = repository.findByIdAndActive("1").orElseThrow();
        assertEquals("Nuevo Título", found.title());
    }

    @Test
    void testUpdateBookNotFound() {
        Book updated = new Book("99", "No existe", "Autor", "000", "desc", true);
        assertTrue(repository.update("99", updated).isEmpty());
    }

    @Test
    void testFindByTemplateAndActive() {
        repository.create(new Book("1", "Java", "Autor1", "111", "desc", true));
        repository.create(new Book("2", "Spring", "Autor2", "222", "desc", true));
        Book template = new Book(null, "Java", null, null, null, null);
        List<Book> result = repository.findByTemplateAndActive(template);
        assertEquals(1, result.size());
        assertEquals("Java", result.getFirst().title());
    }

    @Test
    void testRepositoryIsEmptyInitially() {
        assertTrue(repository.findAllActive().isEmpty());
    }

    @Test
    void testCreateDuplicateId() {
        Book book1 = new Book("1", "A", "B", "X", "", true);
        Book book2 = new Book("1", "C", "D", "Y", "", true);
        repository.create(book1);
        repository.create(book2);
        List<Book> books = repository.findAllActive();
        assertEquals(2, books.size()); // Repository allows duplicate IDs
    }

    @Test
    void testFindByIdAndActiveNull() {
        assertTrue(repository.findByIdAndActive(null).isEmpty());
    }

    @Test
    void testFindByIdAndActiveEmptyString() {
        assertTrue(repository.findByIdAndActive("").isEmpty());
    }

    @Test
    void testUpdateWithNullBook() {
        repository.create(new Book("1", "A", "B", "X", "", true));
        assertTrue(repository.update("1", null).isEmpty());
    }

    @Test
    void testUpdateWithMismatchedId() {
        Book book = new Book("2", "A", "B", "X", "", true);
        repository.create(new Book("1", "A", "B", "X", "", true));
        assertTrue(repository.update("1", book).isEmpty());
    }

    @Test
    void testFindByTemplateAndActiveMultipleMatches() {
        repository.create(new Book("1", "Java", "Autor1", "111", "desc", true));
        repository.create(new Book("2", "Java", "Autor2", "222", "desc", true));
        Book template = new Book(null, "Java", null, null, null, null);
        List<Book> result = repository.findByTemplateAndActive(template);
        assertEquals(2, result.size());
    }

    @Test
    void testFindByTemplateAndActiveNoMatch() {
        repository.create(new Book("1", "Java", "Autor1", "111", "desc", true));
        Book template = new Book(null, "Spring", null, null, null, null);
        List<Book> result = repository.findByTemplateAndActive(template);
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByTemplateAndActiveAllNull() {
        repository.create(new Book("1", "Java", "Autor1", "111", "desc", true));
        repository.create(new Book("2", "Spring", "Autor2", "222", "desc", true));
        Book template = new Book(null, null, null, null, null, null);
        List<Book> result = repository.findByTemplateAndActive(template);
        assertEquals(2, result.size());
    }

    @Test
    void testFindByTemplateAndActivePartialMatch() {
        repository.create(new Book("1", "Java", "Autor1", "111", "desc", true));
        repository.create(new Book("2", "Spring", "Autor2", "222", "desc", true));
        Book template = new Book(null, null, "Autor2", null, null, null);
        List<Book> result = repository.findByTemplateAndActive(template);
        assertEquals(1, result.size());
        assertEquals("Spring", result.getFirst().title());
    }

    @Test
    void testRepositoryStateAfterUpdate() {
        Book book = new Book("1", "A", "B", "X", "", true);
        repository.create(book);
        Book updated = new Book("1", "Updated", "B", "X", "", true);
        repository.update("1", updated);
        Book found = repository.findByIdAndActive("1").orElseThrow();
        assertEquals("Updated", found.title());
    }

    @Test
    void testRepositoryWithManyBooks() {
        for (int i = 0; i < 1000; i++) {
            repository.create(new Book(String.valueOf(i), "Title" + i, "Author", "ISBN" + i, "desc", true));
        }
        List<Book> books = repository.findAllActive();
        assertEquals(1000, books.size());
    }
}
