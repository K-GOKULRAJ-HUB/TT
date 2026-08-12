package com.gokulmart.service;

import com.gokulmart.dao.CartDAO;
import com.gokulmart.dao.ProductDAO;
import com.gokulmart.model.CartItem;
import com.gokulmart.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class CartService {

    private final CartDAO cartDAO = new CartDAO();
    private final ProductDAO productDAO = new ProductDAO();

    public List<CartItem> getCartItems(Long userId) {
        return cartDAO.getCartItemsByUser(userId);
    }

    public void addToCart(Long userId, Long productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be at least 1.");
        }

        Optional<Product> productOpt = productDAO.findById(productId);
        if (productOpt.isEmpty()) {
            throw new IllegalArgumentException("Product not found.");
        }

        Product product = productOpt.get();
        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("Cannot add more than available stock (" + product.getStock() + " available).");
        }

        cartDAO.addOrUpdateItem(userId, productId, quantity);
    }

    public boolean updateCartQuantity(Long cartItemId, Long userId, int newQuantity) {
        if (newQuantity <= 0) {
            return cartDAO.removeItem(cartItemId, userId);
        }

        // Find existing cart item to check product stock
        List<CartItem> items = cartDAO.getCartItemsByUser(userId);
        for (CartItem item : items) {
            if (item.getId().equals(cartItemId)) {
                if (item.getProduct().getStock() < newQuantity) {
                    throw new IllegalArgumentException("Requested quantity exceeds available stock (" + item.getProduct().getStock() + " available).");
                }
                return cartDAO.updateQuantity(cartItemId, userId, newQuantity);
            }
        }
        return false;
    }

    public boolean removeFromCart(Long cartItemId, Long userId) {
        return cartDAO.removeItem(cartItemId, userId);
    }

    public BigDecimal calculateCartTotal(List<CartItem> cartItems) {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            total = total.add(item.getSubtotal());
        }
        return total;
    }

    public void clearCart(Long userId) {
        cartDAO.clearCart(userId);
    }
}
