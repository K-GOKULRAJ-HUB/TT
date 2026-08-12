package com.gokulmart.service;

import com.gokulmart.dao.OrderDAO;
import com.gokulmart.model.CartItem;
import com.gokulmart.model.Order;
import com.gokulmart.model.OrderStatus;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class OrderService {

    private final OrderDAO orderDAO = new OrderDAO();
    private final CartService cartService = new CartService();

    public Order placeOrder(Long buyerId) throws SQLException {
        List<CartItem> cartItems = cartService.getCartItems(buyerId);
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Your shopping cart is empty.");
        }

        // Validate stock for all items
        for (CartItem item : cartItems) {
            if (item.getProduct().getStock() < item.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for item: " + item.getProduct().getName());
            }
        }

        BigDecimal totalAmount = cartService.calculateCartTotal(cartItems);
        return orderDAO.createOrderTransactional(buyerId, totalAmount, cartItems);
    }

    public Optional<Order> getOrderById(Long orderId) {
        return orderDAO.findById(orderId);
    }

    public List<Order> getBuyerOrders(Long buyerId) {
        return orderDAO.findByBuyerId(buyerId);
    }

    public List<Order> getSellerOrders(Long sellerId) {
        return orderDAO.findBySellerId(sellerId);
    }

    public List<Order> getAllOrders() {
        return orderDAO.findAll();
    }

    public boolean updateOrderStatus(Long orderId, OrderStatus newStatus) {
        return orderDAO.updateStatus(orderId, newStatus);
    }

    public int getTotalOrdersCount() {
        return orderDAO.countAll();
    }

    public int getSellerOrdersCount(Long sellerId) {
        return orderDAO.countBySeller(sellerId);
    }
}
