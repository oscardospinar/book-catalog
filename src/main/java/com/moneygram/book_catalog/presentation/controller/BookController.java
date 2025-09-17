package com.moneygram.book_catalog.presentation.controller;

import com.moneygram.book_catalog.application.service.IBookService;
import com.moneygram.book_catalog.presentation.adapter.BookAdapter;
import com.moneygram.book_catalog.presentation.adapter.ResponseWrapperAdapter;
import com.moneygram.book_catalog.presentation.dto.BookRequestDto;
import com.moneygram.book_catalog.presentation.dto.BookResponseDto;
import com.moneygram.book_catalog.presentation.dto.ResponseWrapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {

    private final IBookService service;
    private final ResponseWrapperAdapter<BookResponseDto> wrapperAdapter;
    private final Logger logger = LoggerFactory.getLogger(BookController.class);
    private static final String BOOKS_PATH = "/books/";

    @PostMapping
    public ResponseWrapper<BookResponseDto> saveBook(@RequestBody BookRequestDto request) {
        logger.info("Creating book: {}", request);
        BookResponseDto response = BookAdapter.toDto(service.create(BookAdapter.toModel(request)));
        logger.info("Book created: {}", response);
        return wrapperAdapter.buildResponse(
                response,
                HttpStatus.CREATED,
                HttpMethod.GET + " " + BOOKS_PATH + response.id(),
                BOOKS_PATH);
    }

    @PutMapping("/{id}")
    public ResponseWrapper<BookResponseDto> update(@PathVariable String id, @RequestBody BookRequestDto request) {
        logger.info("Update book: {}", request);
        BookResponseDto response = BookAdapter.toDto(service.update(id, BookAdapter.toModel(request)));
        logger.info("Book updated: {}", response);
        return wrapperAdapter.buildResponse(
                response,
                HttpStatus.OK,
                HttpMethod.GET + " " + BOOKS_PATH + "?title=" + response.title(),
                BOOKS_PATH + id);
    }

    @DeleteMapping("/{id}")
    public ResponseWrapper<BookResponseDto> delete(@PathVariable String id) {
        logger.info("Delete book: {}", id);
        BookResponseDto response = BookAdapter.toDto(service.delete(id));
        return wrapperAdapter.buildResponse(
                response,
                HttpStatus.OK,
                null,
                BOOKS_PATH + id);
    }

    @GetMapping("/{id}")
    public ResponseWrapper<BookResponseDto> getBookById(@PathVariable String id) {
        logger.info("Get book by id: {}", id);
        BookResponseDto response = BookAdapter.toDto(service.findById(id));
        logger.info("Book found: {}", response);
        return wrapperAdapter.buildResponse(
                response,
                HttpStatus.OK,
                null,
                BOOKS_PATH + id);
    }

    @GetMapping
    public ResponseWrapper<List<BookResponseDto>> getAllBooks() {
        logger.info("Get all books");
        List<BookResponseDto> response = service.findAll()
                .stream()
                .map(BookAdapter::toDto)
                .toList();
        logger.info("All Books found: {}", response);
        return wrapperAdapter.buildResponse(
                response,
                HttpStatus.OK,
                null,
                BOOKS_PATH);
    }

    @GetMapping("search")
    public ResponseWrapper<List<BookResponseDto>> getBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String isbn) {

        BookRequestDto request = BookRequestDto.builder()
                .author(author)
                .title(title)
                .isbn(isbn)
                .build();
        logger.info("Get books by: {}", request);
        List<BookResponseDto> response = service.findByTemplate(BookAdapter.toModel(request))
                .stream()
                .map(BookAdapter::toDto)
                .toList();
        logger.info("Books found: {}", response);
        return wrapperAdapter.buildResponse(
                response,
                HttpStatus.OK,
                null,
                BOOKS_PATH);
    }
}

