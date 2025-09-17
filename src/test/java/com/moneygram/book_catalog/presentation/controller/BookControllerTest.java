package com.moneygram.book_catalog.presentation.controller;

import com.moneygram.book_catalog.application.service.IBookService;
import com.moneygram.book_catalog.presentation.adapter.BookAdapter;
import com.moneygram.book_catalog.presentation.adapter.ResponseWrapperAdapter;
import com.moneygram.book_catalog.presentation.dto.BookRequestDto;
import com.moneygram.book_catalog.presentation.dto.BookResponseDto;
import com.moneygram.book_catalog.presentation.dto.ResponseWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.List;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebMvcTest(BookController.class)
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IBookService bookService;

    @MockitoBean
    private ResponseWrapperAdapter<BookResponseDto> wrapperAdapter;

    private BookRequestDto getSampleRequest() {
        return BookRequestDto.builder()
                .title("Title")
                .author("Author")
                .isbn("1234567890")
                .description("desc")
                .build();
    }

    private BookResponseDto getSampleResponse() {
        return BookResponseDto.builder()
                .id("1")
                .title("Title")
                .author("Author")
                .isbn("1234567890")
                .description("desc")
                .active(true)
                .build();
    }

    private ResponseWrapper<BookResponseDto> getSampleWrapper(BookResponseDto dto, HttpStatus status) {
        return ResponseWrapper.<BookResponseDto>builder()
                .data(dto)
                .metadata(null)
                .build();
    }

    @Test
    void testSaveBook() throws Exception {
        BookRequestDto request = getSampleRequest();
        BookResponseDto response = getSampleResponse();
        ResponseWrapper<BookResponseDto> wrapper = getSampleWrapper(response, HttpStatus.CREATED);

        when(bookService.create(any())).thenReturn(BookAdapter.toModel(request));
        when(wrapperAdapter.buildResponse(any(BookResponseDto.class), any(), any(), any())).thenReturn(wrapper);

        mockMvc.perform(MockMvcRequestBuilders.post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Title\",\"author\":\"Author\",\"isbn\":\"1234567890\",\"description\":\"desc\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testUpdateBook() throws Exception {
        BookRequestDto request = getSampleRequest();
        BookResponseDto response = getSampleResponse();
        ResponseWrapper<BookResponseDto> wrapper = getSampleWrapper(response, HttpStatus.OK);
        when(bookService.update(anyString(), any())).thenReturn(BookAdapter.toModel(request));
        when(wrapperAdapter.buildResponse(any(BookResponseDto.class), any(), any(), any())).thenReturn(wrapper);
        mockMvc.perform(MockMvcRequestBuilders.put("/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Title\",\"author\":\"Author\",\"isbn\":\"1234567890\",\"description\":\"desc\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testDeleteBook() throws Exception {
        BookResponseDto response = getSampleResponse();
        ResponseWrapper<BookResponseDto> wrapper = getSampleWrapper(response, HttpStatus.OK);
        when(bookService.delete(anyString())).thenReturn(BookAdapter.toModel(getSampleRequest()));
        when(wrapperAdapter.buildResponse(any(BookResponseDto.class), any(), any(), any())).thenReturn(wrapper);
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetBookById() throws Exception {
        BookResponseDto response = getSampleResponse();
        ResponseWrapper<BookResponseDto> wrapper = getSampleWrapper(response, HttpStatus.OK);
        when(bookService.findById(anyString())).thenReturn(BookAdapter.toModel(getSampleRequest()));
        when(wrapperAdapter.buildResponse(any(BookResponseDto.class), any(), any(), any())).thenReturn(wrapper);
        mockMvc.perform(MockMvcRequestBuilders.get("/books/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetAllBooks() throws Exception {
        BookResponseDto response = getSampleResponse();
        ResponseWrapper<BookResponseDto> wrapper = getSampleWrapper(response, HttpStatus.OK);
        when(bookService.findAll()).thenReturn(List.of(BookAdapter.toModel(getSampleRequest())));
        when(wrapperAdapter.buildResponse(any(BookResponseDto.class), any(), any(), any())).thenReturn(wrapper);
        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetBooksSearch() throws Exception {
        BookResponseDto response = getSampleResponse();
        ResponseWrapper<BookResponseDto> wrapper = getSampleWrapper(response, HttpStatus.OK);
        when(bookService.findByTemplate(any())).thenReturn(List.of(BookAdapter.toModel(getSampleRequest())));
        when(wrapperAdapter.buildResponse(any(BookResponseDto.class), any(), any(), any())).thenReturn(wrapper);
        mockMvc.perform(MockMvcRequestBuilders.get("/books/search")
                .param("title", "Title")
                .param("author", "Author")
                .param("isbn", "1234567890"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
