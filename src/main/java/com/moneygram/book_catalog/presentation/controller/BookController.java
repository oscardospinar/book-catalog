package com.moneygram.book_catalog.presentation.controller;

import com.moneygram.book_catalog.application.service.IBookService;
import com.moneygram.book_catalog.presentation.adapter.BookAdapter;
import com.moneygram.book_catalog.presentation.adapter.ResponseWrapperAdapter;
import com.moneygram.book_catalog.presentation.dto.BookRequestDto;
import com.moneygram.book_catalog.presentation.dto.BookResponseDto;
import com.moneygram.book_catalog.presentation.dto.ResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Books", description = "API for managing books in the catalog")
@AllArgsConstructor
@RestController
@RequestMapping("/books")
@ControllerAdvice
public class BookController {

    private final IBookService service;
    private final ResponseWrapperAdapter<BookResponseDto> wrapperAdapter;
    private final Logger logger = LoggerFactory.getLogger(BookController.class);
    private static final String BOOKS_PATH = "/books/";

    @Operation(summary = "Create Book",
            description = "Creates a new book in the catalog")
    @ApiResponse(responseCode = "200", description = "Book created successfully")
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

    @Operation(summary = "Updates a book",
            description = "Updates a existing book based on the id")
    @ApiResponse(responseCode = "200", description = "Book updated")
    @ApiResponse(responseCode = "404", description = "Libro no encontrado")
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

    @Operation(summary = "Deletes a book",
            description = "Deletes a book based on the id")
    @ApiResponse(responseCode = "200", description = "Deleted book")
    @ApiResponse(responseCode = "404", description = "Book not found")
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

    @Operation(summary = "Gets a book by id",
            description = "Gets a book by id")
    @ApiResponse(responseCode = "200", description = "Found book")
    @ApiResponse(responseCode = "404", description = "Book not found")
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

    @Operation(summary = "Get all the books",
            description = "Get a list with all books")
    @ApiResponse(responseCode = "200", description = "List of all books")
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

    @Operation(summary = "Gets a books from filters",
            description = "Gets a books from filters")
    @ApiResponse(responseCode = "200", description = "Search successful")
    @GetMapping("search")
    public ResponseWrapper<List<BookResponseDto>> getBooks(
            @Parameter(description = "Book title") @RequestParam(required = false) String title,
            @Parameter(description = "Book author") @RequestParam(required = false) String author,
            @Parameter(description = "ISBN")  @RequestParam(required = false) String isbn) {

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

