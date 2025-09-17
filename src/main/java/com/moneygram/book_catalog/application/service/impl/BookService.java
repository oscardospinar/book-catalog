package com.moneygram.book_catalog.application.service.impl;

import com.moneygram.book_catalog.application.exception.EntityCreationException;
import com.moneygram.book_catalog.application.exception.EntityNotFoundException;
import com.moneygram.book_catalog.application.exception.EntityUpdateException;
import com.moneygram.book_catalog.application.service.IBookService;
import com.moneygram.book_catalog.domain.model.Book;
import com.moneygram.book_catalog.domain.repository.IBookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BookService implements IBookService {

    private final IBookRepository bookRepository;

    @Override
    public Book findById(String id) {
        return bookRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
    }

    @Override
    public Book create(Book book) {
        return bookRepository
                .create(book.copyWithId(UUID.randomUUID().toString()))
                .orElseThrow(() -> new EntityCreationException("Exception creating book " + book));
    }

    @Override
    public Book update(String id, Book book) {
        return bookRepository
                .update(id, book.copyWithId(id))
                .orElseThrow(() -> new EntityUpdateException("Exception updating book " + book));
    }

    @Override
    public Book delete(String id) {
        Book book = bookRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        return bookRepository
                .update(id, book.copyWithActive(false))
                .orElseThrow(() -> new EntityUpdateException("Exception updating book " + book));
    }

    @Override
    public List<Book> findByTemplate(Book template) {
        return bookRepository.findByTemplate(template);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }
}
