package com.moneygram.book_catalog.presentation.adapter;

import com.moneygram.book_catalog.presentation.dto.ResponseWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResponseWrapperAdapterTest {

    private ResponseWrapperAdapter<String> adapter;
    private static final String TEST_VERSION = "1.0.0";
    private static final String TEST_PATH = "/books";
    private static final String TEST_NEXT = "next";

    @BeforeEach
    void setUp() {
        adapter = new ResponseWrapperAdapter<>();
        ReflectionTestUtils.setField(adapter, "version", TEST_VERSION);
    }

    @Test
    void testBuildResponseWithSingleItem() {
        ResponseWrapper<String> response = adapter.buildResponse(
                "test data",
                HttpStatus.OK,
                TEST_NEXT,
                TEST_PATH
        );

        assertEquals("test data", response.getData());
        assertNotNull(response.getMetadata());
        assertEquals(HttpStatus.OK.value(), response.getMetadata().status());
        assertEquals(TEST_PATH, response.getMetadata().path());
        assertEquals(TEST_NEXT, response.getMetadata().next());
        assertEquals(TEST_VERSION, response.getMetadata().version());
        assertNotNull(response.getMetadata().timestamp());
    }

    @Test
    void testBuildResponseWithList() {
        List<String> testData = List.of("item1", "item2", "item3");
        ResponseWrapper<List<String>> response = adapter.buildResponse(
                testData,
                HttpStatus.OK,
                TEST_NEXT,
                TEST_PATH
        );

        assertEquals(testData, response.getData());
        assertEquals(3, response.getData().size());
        assertNotNull(response.getMetadata());
        assertEquals(HttpStatus.OK.value(), response.getMetadata().status());
        assertEquals(TEST_PATH, response.getMetadata().path());
        assertEquals(TEST_NEXT, response.getMetadata().next());
        assertEquals(TEST_VERSION, response.getMetadata().version());
        assertNotNull(response.getMetadata().timestamp());
    }

    @Test
    void testBuildResponseWithMessage() {
        String errorMessage = "Error occurred";
        ResponseWrapper<String> response = adapter.buildResponse(
                errorMessage,
                HttpStatus.BAD_REQUEST,
                null,
                TEST_PATH
        );

        assertEquals(errorMessage, response.getData());
        assertNotNull(response.getMetadata());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getMetadata().status());
        assertEquals(TEST_PATH, response.getMetadata().path());
        assertNull(response.getMetadata().next());
        assertEquals(TEST_VERSION, response.getMetadata().version());
        assertNotNull(response.getMetadata().timestamp());
    }

    @Test
    void testBuildResponseWithEmptyList() {
        List<String> emptyList = List.of();
        ResponseWrapper<List<String>> response = adapter.buildResponse(
                emptyList,
                HttpStatus.OK,
                TEST_NEXT,
                TEST_PATH
        );

        assertTrue(response.getData().isEmpty());
        assertNotNull(response.getMetadata());
        assertEquals(HttpStatus.OK.value(), response.getMetadata().status());
        assertEquals(TEST_PATH, response.getMetadata().path());
        assertEquals(TEST_NEXT, response.getMetadata().next());
        assertEquals(TEST_VERSION, response.getMetadata().version());
        assertNotNull(response.getMetadata().timestamp());
    }
}
