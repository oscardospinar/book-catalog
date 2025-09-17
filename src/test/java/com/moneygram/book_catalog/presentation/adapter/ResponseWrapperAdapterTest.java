package com.moneygram.book_catalog.presentation.adapter;

import com.moneygram.book_catalog.presentation.dto.Metadata;
import com.moneygram.book_catalog.presentation.dto.ResponseWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import static org.junit.jupiter.api.Assertions.*;

class ResponseWrapperAdapterTest {
    @Test
    void testBuildResponse() {
        ResponseWrapperAdapter<String> adapter = new ResponseWrapperAdapter<>();
        ResponseWrapper<String> response = adapter.buildResponse("data", HttpStatus.OK, "next", "/books");
        assertEquals("data", response.getData());
        assertNotNull(response.getMetadata());
        assertEquals(HttpStatus.OK.value(), response.getMetadata().status());
        assertEquals("/books", response.getMetadata().path());
        assertEquals("next", response.getMetadata().next());
    }
}

