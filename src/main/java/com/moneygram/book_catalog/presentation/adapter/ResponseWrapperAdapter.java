package com.moneygram.book_catalog.presentation.adapter;

import com.moneygram.book_catalog.presentation.dto.Metadata;
import com.moneygram.book_catalog.presentation.dto.ResponseWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class ResponseWrapperAdapter<T> {

    @Value("${app.version:1.0.0}")
    private String version;

    public ResponseWrapper<T> buildResponse(T response, HttpStatus statusCode, String next, String path) {
        return ResponseWrapper
                .<T>builder()
                .data(response)
                .metadata(Metadata.builder()
                        .timestamp(Instant.now().toString())
                        .path(path)
                        .version(version)
                        .status(statusCode.value())
                        .next(next)
                        .build())
                .build();
    }

    public ResponseWrapper<List<T>> buildResponse(List<T> response, HttpStatus statusCode, String next, String path) {
        return ResponseWrapper
                .<List<T>>builder()
                .data(response)
                .metadata(Metadata.builder()
                        .timestamp(Instant.now().toString())
                        .path(path)
                        .version(version)
                        .status(statusCode.value())
                        .next(next)
                        .build())
                .build();
    }
}
