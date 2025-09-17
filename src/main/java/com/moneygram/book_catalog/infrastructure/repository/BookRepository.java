package com.moneygram.book_catalog.infrastructure.repository;

import com.moneygram.book_catalog.domain.model.Book;
import com.moneygram.book_catalog.domain.repository.IBookRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class BookRepository implements IBookRepository {
    private List<Book> books = new ArrayList<>();

    @Override
    public Optional<Book> findByIdAndActive(String id) {
        return books
                .stream()
                .filter(book -> book.active() &&  id.equals(book.id()))
                .findAny();
    }

    @Override
    public Optional<Book> create(Book book) {
        books.add(book);
        return Optional.of(book);
    }

    @Override
    public Optional<Book> update(String id, Book book) {
        if(book == null || id == null || !id.equals(book.id())) {
            return Optional.empty();
        }

        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).id().equals(id)) {
                books.set(i, book);
                return Optional.of(book);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Book> findByTemplateAndActive(Book template) {
        return books
                .stream()
                .filter(book -> book.active() && matchesTemplate(book, template))
                .toList();
    }

    @Override
    public List<Book> findAllActive() {
        return books.stream().filter(Book::active).toList();
    }

    private boolean matchesTemplate(Book book, Book template) {
        return (template.id() == null || template.id().equals(book.id())) &&
                (template.title() == null || book.title().toLowerCase().contains(template.title().toLowerCase())) &&
                (template.author() == null || book.author().toLowerCase().contains(template.author().toLowerCase())) &&
                (template.isbn() == null || book.isbn().equals(template.isbn())) &&
                (template.active() == null || template.active().equals(book.active()));
    }
}
