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

    @Operation(summary = "Actualizar un libro",
            description = "Actualiza un libro existente por su ID")
    @ApiResponse(responseCode = "200", description = "Libro actualizado exitosamente")
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

    @Operation(summary = "Eliminar un libro",
            description = "Elimina un libro por su ID")
    @ApiResponse(responseCode = "200", description = "Libro eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Libro no encontrado")
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

    @Operation(summary = "Obtener libro por ID",
            description = "Busca un libro por su ID")
    @ApiResponse(responseCode = "200", description = "Libro encontrado")
    @ApiResponse(responseCode = "404", description = "Libro no encontrado")
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

    @Operation(summary = "Obtener todos los libros",
            description = "Obtiene la lista completa de libros")
    @ApiResponse(responseCode = "200", description = "Lista de libros recuperada exitosamente")
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

    @Operation(summary = "Buscar libros por criterios",
            description = "Busca libros que coincidan con los criterios especificados")
    @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente")
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

