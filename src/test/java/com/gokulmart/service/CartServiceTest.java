package com.gokulmart.service;

import com.gokulmart.model.CartItem;
import com.gokulmart.model.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CartServiceTest {

    private final CartService cartService = new CartService();

    @Test
    public void testCalculateCartTotal() {
        List<CartItem> items = new ArrayList<>();

        Product p1 = new Product();
        p1.setPrice(new BigDecimal("500.00"));

        CartItem item1 = new CartItem(1L, 1L, 101L, 3);
        item1.setProduct(p1);
        items.add(item1);

        Product p2 = new Product();
        p2.setPrice(new BigDecimal("250.00"));

        CartItem item2 = new CartItem(2L, 1L, 102L, 2);
        item2.setProduct(p2);
        items.add(item2);

        BigDecimal total = cartService.calculateCartTotal(items);
        // (500 * 3) + (250 * 2) = 1500 + 500 = 2000.00
        assertEquals(new BigDecimal("2000.00"), total, "Cart total calculation should equal 2000.00");
    }
}
