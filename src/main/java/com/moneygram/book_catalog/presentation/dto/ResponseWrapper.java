package com.moneygram.book_catalog.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ResponseWrapper<T> {
    private final T data;
    private final Metadata metadata;
}
