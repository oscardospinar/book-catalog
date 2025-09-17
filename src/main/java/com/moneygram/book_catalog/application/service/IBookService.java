package com.moneygram.book_catalog.application.service;

import com.moneygram.book_catalog.domain.model.Book;

import java.util.Arrays;
import java.util.List;

public interface IBookService {
    Book findById(String id);

    Book create(Book book);

    Book update(String id, Book book);

    Book delete(String id);

    List<Book> findByTemplate(Book model);

    List<Book> findAll();
}
