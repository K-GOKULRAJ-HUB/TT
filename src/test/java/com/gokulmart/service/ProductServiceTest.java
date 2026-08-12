package com.gokulmart.service;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest {

    private final ProductService productService = new ProductService();

    @Test
    public void testCreateProductValidation() {
        assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(1L, "", "Desc", new BigDecimal("100.00"), 10, "Electronics", "");
        }, "Blank product name should throw IllegalArgumentException");

        assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(1L, "Valid Name", "Desc", new BigDecimal("-50.00"), 10, "Electronics", "");
        }, "Negative or zero price should throw IllegalArgumentException");

        assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(1L, "Valid Name", "Desc", new BigDecimal("100.00"), -5, "Electronics", "");
        }, "Negative stock should throw IllegalArgumentException");
    }
}
