package com.gokulmart.dao;

import com.gokulmart.model.*;
import com.gokulmart.util.DBUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDAO {

    private final ProductDAO productDAO = new ProductDAO();
    private final CartDAO cartDAO = new CartDAO();

    public Order createOrderTransactional(Long buyerId, BigDecimal totalAmount, List<CartItem> cartItems) throws SQLException {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false); // Begin transaction

            // 1. Create order record
            String orderSql = "INSERT INTO orders (buyer_id, total_amount, status) VALUES (?, ?, ?)";
            Long orderId = null;
            try (PreparedStatement psOrder = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                psOrder.setLong(1, buyerId);
                psOrder.setBigDecimal(2, totalAmount);
                psOrder.setString(3, OrderStatus.PENDING.name());
                psOrder.executeUpdate();

                try (ResultSet rs = psOrder.getGeneratedKeys()) {
                    if (rs.next()) {
                        orderId = rs.getLong(1);
                    }
                }
            }

            if (orderId == null) {
                throw new SQLException("Failed to create order, no ID returned.");
            }

            // 2. Insert order items & reduce product stock atomically
            String itemSql = "INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
            try (PreparedStatement psItem = conn.prepareStatement(itemSql)) {
                for (CartItem item : cartItems) {
                    boolean stockDeducted = productDAO.updateStockTransactional(conn, item.getProductId(), item.getQuantity());
                    if (!stockDeducted) {
                        throw new SQLException("Insufficient stock for product: " + item.getProduct().getName());
                    }

                    psItem.setLong(1, orderId);
                    psItem.setLong(2, item.getProductId());
                    psItem.setInt(3, item.getQuantity());
                    psItem.setBigDecimal(4, item.getProduct().getPrice());
                    psItem.addBatch();
                }
                psItem.executeBatch();
            }

            // 3. Clear buyer cart
            cartDAO.clearCartTransactional(conn, buyerId);

            conn.commit(); // Commit transaction

            Order order = new Order();
            order.setId(orderId);
            order.setBuyerId(buyerId);
            order.setTotalAmount(totalAmount);
            order.setStatus(OrderStatus.PENDING);
            return order;

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw new SQLException("Transaction failed during checkout: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Optional<Order> findById(Long id) {
        String sql = "SELECT o.*, u.name as buyer_name, u.email as buyer_email " +
                     "FROM orders o JOIN users u ON o.buyer_id = u.id WHERE o.id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    order.setItems(getOrderItems(conn, id));
                    return Optional.of(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Order> findByBuyerId(Long buyerId) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, u.name as buyer_name, u.email as buyer_email " +
                     "FROM orders o JOIN users u ON o.buyer_id = u.id WHERE o.buyer_id = ? ORDER BY o.created_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, buyerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    order.setItems(getOrderItems(conn, order.getId()));
                    list.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Order> findBySellerId(Long sellerId) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT DISTINCT o.*, u.name as buyer_name, u.email as buyer_email " +
                     "FROM orders o " +
                     "JOIN users u ON o.buyer_id = u.id " +
                     "JOIN order_items oi ON o.id = oi.order_id " +
                     "JOIN products p ON oi.product_id = p.id " +
                     "WHERE p.seller_id = ? ORDER BY o.created_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    order.setItems(getSellerOrderItems(conn, order.getId(), sellerId));
                    list.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Order> findAll() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, u.name as buyer_name, u.email as buyer_email " +
                     "FROM orders o JOIN users u ON o.buyer_id = u.id ORDER BY o.created_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                order.setItems(getOrderItems(conn, order.getId()));
                list.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateStatus(Long orderId, OrderStatus status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setLong(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int countAll() {
        String sql = "SELECT COUNT(*) FROM orders";
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
        String sql = "SELECT COUNT(DISTINCT o.id) FROM orders o " +
                     "JOIN order_items oi ON o.id = oi.order_id " +
                     "JOIN products p ON oi.product_id = p.id WHERE p.seller_id = ?";
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

    private List<OrderItem> getOrderItems(Connection conn, Long orderId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT oi.*, p.name as p_name, p.category, p.image_url, p.seller_id, u.name as seller_name " +
                     "FROM order_items oi " +
                     "JOIN products p ON oi.product_id = p.id " +
                     "JOIN users u ON p.seller_id = u.id " +
                     "WHERE oi.order_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = mapResultSetToOrderItem(rs);
                    items.add(item);
                }
            }
        }
        return items;
    }

    private List<OrderItem> getSellerOrderItems(Connection conn, Long orderId, Long sellerId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT oi.*, p.name as p_name, p.category, p.image_url, p.seller_id, u.name as seller_name " +
                     "FROM order_items oi " +
                     "JOIN products p ON oi.product_id = p.id " +
                     "JOIN users u ON p.seller_id = u.id " +
                     "WHERE oi.order_id = ? AND p.seller_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            ps.setLong(2, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = mapResultSetToOrderItem(rs);
                    items.add(item);
                }
            }
        }
        return items;
    }

    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setId(rs.getLong("id"));
        o.setBuyerId(rs.getLong("buyer_id"));
        o.setBuyerName(rs.getString("buyer_name"));
        o.setBuyerEmail(rs.getString("buyer_email"));
        o.setTotalAmount(rs.getBigDecimal("total_amount"));
        o.setStatus(OrderStatus.valueOf(rs.getString("status")));
        o.setCreatedAt(rs.getTimestamp("created_at"));
        return o;
    }

    private OrderItem mapResultSetToOrderItem(ResultSet rs) throws SQLException {
        OrderItem item = new OrderItem();
        item.setId(rs.getLong("id"));
        item.setOrderId(rs.getLong("order_id"));
        item.setProductId(rs.getLong("product_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setUnitPrice(rs.getBigDecimal("unit_price"));

        Product p = new Product();
        p.setId(rs.getLong("product_id"));
        p.setName(rs.getString("p_name"));
        p.setCategory(rs.getString("category"));
        p.setImageUrl(rs.getString("image_url"));
        p.setSellerId(rs.getLong("seller_id"));
        p.setSellerName(rs.getString("seller_name"));
        p.setPrice(rs.getBigDecimal("unit_price"));

        item.setProduct(p);
        return item;
    }
}
