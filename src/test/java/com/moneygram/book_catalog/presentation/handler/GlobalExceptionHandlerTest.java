package com.moneygram.book_catalog.presentation.handler;

import com.moneygram.book_catalog.application.exception.EntityCreationException;
import com.moneygram.book_catalog.application.exception.EntityNotFoundException;
import com.moneygram.book_catalog.application.exception.EntityUpdateException;
import com.moneygram.book_catalog.presentation.adapter.ResponseWrapperAdapter;
import com.moneygram.book_catalog.presentation.dto.ResponseWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private ResponseWrapperAdapter<String> wrapperAdapter;

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler(wrapperAdapter);
    }

    @Test
    void handleEntityNotFoundException() {
        EntityNotFoundException ex = new EntityNotFoundException("Entity not found");
        ResponseWrapper<String> wrapper = new ResponseWrapper<>("Entity not found", null);

        when(wrapperAdapter.buildResponse(anyString(), any(), any(), any())).thenReturn(wrapper);

        ResponseEntity<ResponseWrapper<String>> response = handler.handleEntityNotFoundException(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Entity not found", response.getBody().getData());
    }

    @Test
    void handleEntityCreationException() {
        EntityCreationException ex = new EntityCreationException("Creation error");
        ResponseWrapper<String> wrapper = new ResponseWrapper<>("Creating entity exception: ", null);

        when(wrapperAdapter.buildResponse(anyString(), any(), any(), any())).thenReturn(wrapper);

        ResponseEntity<ResponseWrapper<String>> response = handler.handleEntityCreationException(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Creating entity exception: ", response.getBody().getData());
    }

    @Test
    void handleEntityUpdateException() {
        EntityUpdateException ex = new EntityUpdateException("Update error");
        ResponseWrapper<String> wrapper = new ResponseWrapper<>("Updating entity exception: ", null);

        when(wrapperAdapter.buildResponse(anyString(), any(), any(), any())).thenReturn(wrapper);

        ResponseEntity<ResponseWrapper<String>> response = handler.handleEntityUpdateException(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updating entity exception: ", response.getBody().getData());
    }

    @Test
    void handleGenericException() {
        Exception ex = new RuntimeException("Unexpected error");
        ResponseWrapper<String> wrapper = new ResponseWrapper<>("Unexpected exception: ", null);

        when(wrapperAdapter.buildResponse(anyString(), any(), any(), any())).thenReturn(wrapper);

        ResponseEntity<ResponseWrapper<String>> response = handler.handleEntityUpdateException(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Unexpected exception: ", response.getBody().getData());
    }
}
