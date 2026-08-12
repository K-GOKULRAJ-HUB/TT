package com.gokulmart.dao;

import com.gokulmart.model.Review;
import com.gokulmart.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    public Review create(Review review) {
        String sql = "INSERT INTO reviews (user_id, product_id, rating, comment) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, review.getUserId());
            ps.setLong(2, review.getProductId());
            ps.setInt(3, review.getRating());
            ps.setString(4, review.getComment() != null ? review.getComment().trim() : "");
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    review.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving review: " + e.getMessage(), e);
        }
        return review;
    }

    public List<Review> findByProductId(Long productId) {
        List<Review> list = new ArrayList<>();
        String sql = "SELECT r.*, u.name as user_name FROM reviews r JOIN users u ON r.user_id = u.id WHERE r.product_id = ? ORDER BY r.created_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Review r = new Review();
                    r.setId(rs.getLong("id"));
                    r.setUserId(rs.getLong("user_id"));
                    r.setUserName(rs.getString("user_name"));
                    r.setProductId(rs.getLong("product_id"));
                    r.setRating(rs.getInt("rating"));
                    r.setComment(rs.getString("comment"));
                    r.setCreatedAt(rs.getTimestamp("created_at"));
                    list.add(r);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public double getAverageRating(Long productId) {
        String sql = "SELECT AVG(CAST(rating AS DOUBLE)) FROM reviews WHERE product_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public boolean hasUserPurchasedProduct(Long userId, Long productId) {
        String sql = "SELECT COUNT(*) FROM orders o JOIN order_items oi ON o.id = oi.order_id WHERE o.buyer_id = ? AND oi.product_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
