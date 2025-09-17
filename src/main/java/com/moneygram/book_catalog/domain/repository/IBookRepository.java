package com.moneygram.book_catalog.domain.repository;

import com.moneygram.book_catalog.domain.model.Book;

import java.util.List;
import java.util.Optional;

public interface IBookRepository {
    Optional<Book> findByIdAndActive(String id);

    Optional<Book> create(Book book);

    Optional<Book> update(String id, Book book);

    List<Book> findByTemplateAndActive(Book template);

    List<Book> findAllActive();
}
