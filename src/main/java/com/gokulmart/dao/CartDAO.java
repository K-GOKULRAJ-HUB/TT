package com.gokulmart.dao;

import com.gokulmart.model.CartItem;
import com.gokulmart.model.Product;
import com.gokulmart.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CartDAO {

    public List<CartItem> getCartItemsByUser(Long userId) {
        List<CartItem> items = new ArrayList<>();
        String sql = "SELECT c.*, p.id as p_id, p.seller_id, p.name as p_name, p.description, p.price, p.stock, p.category, p.image_url, u.name as seller_name " +
                     "FROM cart_items c " +
                     "JOIN products p ON c.product_id = p.id " +
                     "JOIN users u ON p.seller_id = u.id " +
                     "WHERE c.user_id = ? ORDER BY c.id ASC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CartItem item = new CartItem();
                    item.setId(rs.getLong("id"));
                    item.setUserId(rs.getLong("user_id"));
                    item.setProductId(rs.getLong("product_id"));
                    item.setQuantity(rs.getInt("quantity"));

                    Product p = new Product();
                    p.setId(rs.getLong("p_id"));
                    p.setSellerId(rs.getLong("seller_id"));
                    p.setSellerName(rs.getString("seller_name"));
                    p.setName(rs.getString("p_name"));
                    p.setDescription(rs.getString("description"));
                    p.setPrice(rs.getBigDecimal("price"));
                    p.setStock(rs.getInt("stock"));
                    p.setCategory(rs.getString("category"));
                    p.setImageUrl(rs.getString("image_url"));

                    item.setProduct(p);
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public void addOrUpdateItem(Long userId, Long productId, int quantity) {
        String checkSql = "SELECT id, quantity FROM cart_items WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement psCheck = conn.prepareStatement(checkSql)) {
            psCheck.setLong(1, userId);
            psCheck.setLong(2, productId);
            try (ResultSet rs = psCheck.executeQuery()) {
                if (rs.next()) {
                    Long cartId = rs.getLong("id");
                    int newQty = rs.getInt("quantity") + quantity;
                    String updateSql = "UPDATE cart_items SET quantity = ? WHERE id = ?";
                    try (PreparedStatement psUpdate = conn.prepareStatement(updateSql)) {
                        psUpdate.setInt(1, newQty);
                        psUpdate.setLong(2, cartId);
                        psUpdate.executeUpdate();
                    }
                } else {
                    String insertSql = "INSERT INTO cart_items (user_id, product_id, quantity) VALUES (?, ?, ?)";
                    try (PreparedStatement psInsert = conn.prepareStatement(insertSql)) {
                        psInsert.setLong(1, userId);
                        psInsert.setLong(2, productId);
                        psInsert.setInt(3, quantity);
                        psInsert.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update cart: " + e.getMessage(), e);
        }
    }

    public boolean updateQuantity(Long cartItemId, Long userId, int quantity) {
        String sql = "UPDATE cart_items SET quantity = ? WHERE id = ? AND user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setLong(2, cartItemId);
            ps.setLong(3, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean removeItem(Long cartItemId, Long userId) {
        String sql = "DELETE FROM cart_items WHERE id = ? AND user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, cartItemId);
            ps.setLong(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void clearCart(Long userId) {
        String sql = "DELETE FROM cart_items WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearCartTransactional(Connection conn, Long userId) throws SQLException {
        String sql = "DELETE FROM cart_items WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.executeUpdate();
        }
    }
}
