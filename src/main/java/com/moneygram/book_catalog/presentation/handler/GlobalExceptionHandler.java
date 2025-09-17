package com.moneygram.book_catalog.presentation.handler;

import com.moneygram.book_catalog.application.exception.EntityCreationException;
import com.moneygram.book_catalog.application.exception.EntityNotFoundException;
import com.moneygram.book_catalog.application.exception.EntityUpdateException;
import com.moneygram.book_catalog.presentation.adapter.ResponseWrapperAdapter;
import com.moneygram.book_catalog.presentation.dto.ResponseWrapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final ResponseWrapperAdapter<String> wrapperAdapter;


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseWrapper<String>> handleEntityNotFoundException(EntityNotFoundException ex) {
        logger.warn("Not found entity: ", ex);
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.
                status(status).
                body(wrapperAdapter.buildResponse("Entity not found", status, null, null));
    }

    @ExceptionHandler(EntityCreationException.class)
    public ResponseEntity<ResponseWrapper<String>> handleEntityCreationException(EntityCreationException ex) {
        logger.error("Creating entity exception: ", ex);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.
                status(status).
                body(wrapperAdapter.buildResponse("Creating entity exception: ", status, null, null));
    }

    @ExceptionHandler(EntityUpdateException.class)
    public ResponseEntity<ResponseWrapper<String>> handleEntityUpdateException(EntityUpdateException ex) {
        logger.error("Updating entity exception: ", ex);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.
                status(status).
                body(wrapperAdapter.buildResponse("Updating entity exception: ", status, null, null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseWrapper<String>> handleEntityUpdateException(Exception ex) {
        logger.error("Unexpected exception: ", ex);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.
                status(status).
                body(wrapperAdapter.buildResponse("Unexpected exception: ", status, null, null));
    }

}
