package com.moneygram.book_catalog.presentation.dto;

import lombok.Builder;

@Builder
public record Metadata (String version, String timestamp, String path, Integer status, String next) {
}
