package com.gokulmart.dao;

import com.gokulmart.model.Product;
import com.gokulmart.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDAO {

    public Optional<Product> findById(Long id) {
        String sql = "SELECT p.*, u.name as seller_name FROM products p JOIN users u ON p.seller_id = u.id WHERE p.id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Product> findAll() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, u.name as seller_name FROM products p JOIN users u ON p.seller_id = u.id ORDER BY p.created_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Product> search(String query, String category) {
        List<Product> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT p.*, u.name as seller_name FROM products p JOIN users u ON p.seller_id = u.id WHERE 1=1 ");
        
        List<Object> params = new ArrayList<>();
        if (query != null && !query.trim().isEmpty()) {
            sql.append("AND (LOWER(p.name) LIKE ? OR LOWER(p.description) LIKE ?) ");
            String q = "%" + query.trim().toLowerCase() + "%";
            params.add(q);
            params.add(q);
        }
        if (category != null && !category.trim().isEmpty() && !"ALL".equalsIgnoreCase(category)) {
            sql.append("AND LOWER(p.category) = ? ");
            params.add(category.trim().toLowerCase());
        }
        sql.append("ORDER BY p.created_at DESC");

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Product> findBySellerId(Long sellerId) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, u.name as seller_name FROM products p JOIN users u ON p.seller_id = u.id WHERE p.seller_id = ? ORDER BY p.created_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Product create(Product product) {
        String sql = "INSERT INTO products (seller_id, name, description, price, stock, category, image_url) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, product.getSellerId());
            ps.setString(2, product.getName().trim());
            ps.setString(3, product.getDescription() != null ? product.getDescription().trim() : "");
            ps.setBigDecimal(4, product.getPrice());
            ps.setInt(5, product.getStock());
            ps.setString(6, product.getCategory().trim());
            ps.setString(7, product.getImageUrl() != null ? product.getImageUrl().trim() : "");
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    product.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating product: " + e.getMessage(), e);
        }
        return product;
    }

    public boolean update(Product product) {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, stock = ?, category = ?, image_url = ? WHERE id = ? AND seller_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getName().trim());
            ps.setString(2, product.getDescription() != null ? product.getDescription().trim() : "");
            ps.setBigDecimal(3, product.getPrice());
            ps.setInt(4, product.getStock());
            ps.setString(5, product.getCategory().trim());
            ps.setString(6, product.getImageUrl() != null ? product.getImageUrl().trim() : "");
            ps.setLong(7, product.getId());
            ps.setLong(8, product.getSellerId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(Long id, Long sellerId) {
        String sql = "DELETE FROM products WHERE id = ?";
        if (sellerId != null) {
            sql += " AND seller_id = ?";
        }
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            if (sellerId != null) {
                ps.setLong(2, sellerId);
            }
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateStockTransactional(Connection conn, Long productId, int quantityToDeduct) throws SQLException {
        String sql = "UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantityToDeduct);
            ps.setLong(2, productId);
            ps.setInt(3, quantityToDeduct);
            int updatedRows = ps.executeUpdate();
            return updatedRows > 0;
        }
    }

    public int countAll() {
        String sql = "SELECT COUNT(*) FROM products";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countBySeller(Long sellerId) {
        String sql = "SELECT COUNT(*) FROM products WHERE seller_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
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
        return p;
    }
}
