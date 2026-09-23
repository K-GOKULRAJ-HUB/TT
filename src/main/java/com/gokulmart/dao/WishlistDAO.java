package com.gokulmart.dao;

import com.gokulmart.model.Product;
import com.gokulmart.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WishlistDAO {

    public boolean addProductToWishlist(long userId, long productId) {
        String sql = "INSERT INTO wishlists (user_id, product_id) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            // Might fail if already exists due to unique constraint, that's okay
            e.printStackTrace();
        }
        return false;
    }

    public boolean removeProductFromWishlist(long userId, long productId) {
        String sql = "DELETE FROM wishlists WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Product> getWishlistByUser(long userId) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, u.name as seller_name FROM products p " +
                     "JOIN wishlists w ON p.id = w.product_id " +
                     "JOIN users u ON p.seller_id = u.id " +
                     "WHERE w.user_id = ? " +
                     "ORDER BY w.created_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product();
                    p.setId(rs.getLong("id"));
                    p.setSellerId(rs.getLong("seller_id"));
                    p.setSellerName(rs.getString("seller_name"));
                    p.setName(rs.getString("name"));
                    p.setDescription(rs.getString("description"));
                    p.setPrice(rs.getBigDecimal("price"));
                    p.setStock(rs.getInt("stock"));
                    p.setCategory(rs.getString("category"));
                    p.setImageUrl(rs.getString("image_url"));
                    p.setCreatedAt(rs.getTimestamp("created_at"));
                    p.setWishlisted(true);
                    products.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
    
    public Set<Long> getWishlistedProductIdsByUser(long userId) {
        Set<Long> productIds = new HashSet<>();
        String sql = "SELECT product_id FROM wishlists WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    productIds.add(rs.getLong("product_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productIds;
    }
}
