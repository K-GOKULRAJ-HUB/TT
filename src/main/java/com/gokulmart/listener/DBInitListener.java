package com.gokulmart.listener;

import com.gokulmart.model.Role;
import com.gokulmart.model.User;
import com.gokulmart.util.DBUtil;
import com.gokulmart.util.PasswordUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.stream.Collectors;

@WebListener
public class DBInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("[Gokul Mart] Initializing H2 Database and Seeding Data...");
        try (Connection conn = DBUtil.getConnection()) {
            // 1. Run DDL schema script
            runScript(conn, "schema.sql");

            // 2. Check if database has users
            boolean emptyUsers = true;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users")) {
                if (rs.next() && rs.getInt(1) > 0) {
                    emptyUsers = false;
                }
            }

            if (emptyUsers) {
                System.out.println("[Gokul Mart] Seeding default users, products, categories, orders, and reviews...");
                seedData(conn);
            } else {
                System.out.println("[Gokul Mart] Database already initialized with seed data.");
            }
        } catch (Exception e) {
            System.err.println("[Gokul Mart] Database Initialization Failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void runScript(Connection conn, String scriptName) throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream(scriptName);
        if (is == null) {
            System.err.println("[Gokul Mart] Could not find resource: " + scriptName);
            return;
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String sql = reader.lines().collect(Collectors.joining("\n"));
            String[] statements = sql.split(";");
            try (Statement stmt = conn.createStatement()) {
                for (String statement : statements) {
                    if (!statement.trim().isEmpty()) {
                        stmt.execute(statement.trim());
                    }
                }
            }
        }
    }

    private void seedData(Connection conn) throws Exception {
        // Passwords
        String adminHash = PasswordUtil.hashPassword("admin123");
        String sellerHash = PasswordUtil.hashPassword("seller123");
        String buyerHash = PasswordUtil.hashPassword("buyer123");

        // Seed Users
        long adminId = insertUser(conn, "Admin User", "admin@gokulmart.com", adminHash, Role.ADMIN);
        long seller1Id = insertUser(conn, "TechGear Official", "seller1@gokulmart.com", sellerHash, Role.SELLER);
        long seller2Id = insertUser(conn, "Apex Publishers & Wear", "seller2@gokulmart.com", sellerHash, Role.SELLER);
        long buyer1Id = insertUser(conn, "Gokulraj", "buyer1@gokulmart.com", buyerHash, Role.BUYER);
        long buyer2Id = insertUser(conn, "Ananya Sharma", "buyer2@gokulmart.com", buyerHash, Role.BUYER);

        // Seed Products (Accessories, Books, Clothing, Electronics, Home)
        // Electronics
        long p1 = insertProduct(conn, seller1Id, "SoundPulse Pro Wireless Earbuds", "Active noise cancelling wireless earbuds with 30-hour battery life and Bluetooth 5.3.", new BigDecimal("2499.00"), 45, "Electronics", "https://images.unsplash.com/photo-1590658268037-6bf12165a8df?w=600&auto=format&fit=crop");
        long p2 = insertProduct(conn, seller1Id, "Quantum Mechanical Gaming Keyboard", "Tactile RGB back-lit mechanical keyboard with hot-swappable switches and aluminum frame.", new BigDecimal("4999.00"), 20, "Electronics", "https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=600&auto=format&fit=crop");
        long p3 = insertProduct(conn, seller1Id, "UltraSharp 27-inch 4K HDR Monitor", "Ultra-thin IPS bezel display with 99% sRGB color accuracy for professionals.", new BigDecimal("22499.00"), 12, "Electronics", "https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=600&auto=format&fit=crop");
        long p4 = insertProduct(conn, seller1Id, "Aura Precision RGB Wireless Mouse", "Ultra-lightweight ergonomic optical gaming mouse with 26000 DPI sensor.", new BigDecimal("1899.00"), 35, "Electronics", "https://images.unsplash.com/photo-1615663245857-ac93bb7c39e7?w=600&auto=format&fit=crop");

        // Books
        long p5 = insertProduct(conn, seller2Id, "Clean Code Capstone Edition", "A handbook of agile software craftsmanship, design patterns, and refactoring guidelines.", new BigDecimal("899.00"), 50, "Books", "https://images.unsplash.com/photo-1532012197267-da84d127e765?w=600&auto=format&fit=crop");
        long p6 = insertProduct(conn, seller2Id, "The Pragmatic Developer Guide", "Journey from journeyman to master software engineer with practical engineering insights.", new BigDecimal("749.00"), 40, "Books", "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=600&auto=format&fit=crop");
        long p7 = insertProduct(conn, seller2Id, "Mastering Modern Java 17", "Deep dive into modern Java 17 features, design architectures, and backend concurrency.", new BigDecimal("1199.00"), 30, "Books", "https://images.unsplash.com/photo-1512820790803-83ca734da794?w=600&auto=format&fit=crop");

        // Clothing
        long p8 = insertProduct(conn, seller2Id, "Midnight Urban Heavyweight Hoodie", "Premium 400 GSM fleece cotton hoodie with reinforced ribbed cuffs in deep midnight navy.", new BigDecimal("1999.00"), 25, "Clothing", "https://images.unsplash.com/photo-1556905055-8f358a7a47b2?w=600&auto=format&fit=crop");
        long p9 = insertProduct(conn, seller2Id, "Classic Oxford Button-Down Shirt", "Wrinkle-resistant 100% breathable cotton shirt tailored for business and casual wear.", new BigDecimal("1499.00"), 30, "Clothing", "https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=600&auto=format&fit=crop");

        // Accessories
        long p10 = insertProduct(conn, seller1Id, "Titan Steel Minimalist Chronograph Watch", "Sleek stainless steel quartz watch featuring scratch-resistant sapphire crystal glass.", new BigDecimal("3499.00"), 18, "Accessories", "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600&auto=format&fit=crop");
        long p11 = insertProduct(conn, seller1Id, "Genuine Top-Grain Leather Bifold Wallet", "RFID-blocking handcrafted leather wallet with multiple card slots and cash divider.", new BigDecimal("999.00"), 60, "Accessories", "https://images.unsplash.com/photo-1627123424574-724758594e93?w=600&auto=format&fit=crop");

        // Home
        long p12 = insertProduct(conn, seller2Id, "ErgoComfort Breathable Mesh Desk Chair", "High-back ergonomic office chair with adjustable lumbar support and 3D armrests.", new BigDecimal("8999.00"), 15, "Home", "https://images.unsplash.com/photo-1580481072645-022f9a6d8310?w=600&auto=format&fit=crop");
        long p13 = insertProduct(conn, seller2Id, "Smart Ambient LED Desk Lamp", "Touch-controlled dimmable desk lamp with eye-care diffuse lighting and wireless phone charger.", new BigDecimal("2299.00"), 40, "Home", "https://images.unsplash.com/photo-1534349762230-e0cadf78f5da?w=600&auto=format&fit=crop");

        // Seed Sample Orders
        long o1 = insertOrder(conn, buyer1Id, new BigDecimal("3398.00"), "DELIVERED");
        insertOrderItem(conn, o1, p1, 1, new BigDecimal("2499.00"));
        insertOrderItem(conn, o1, p5, 1, new BigDecimal("899.00"));

        long o2 = insertOrder(conn, buyer2Id, new BigDecimal("4999.00"), "SHIPPED");
        insertOrderItem(conn, o2, p2, 1, new BigDecimal("4999.00"));

        // Seed Sample Reviews
        insertReview(conn, buyer1Id, p1, 5, "Outstanding sound quality and deep bass! The ANC works like magic.");
        insertReview(conn, buyer1Id, p5, 5, "Every developer must read this book. Clear explanations and great examples.");
        insertReview(conn, buyer2Id, p2, 4, "Keycaps feel amazing and RGB customizability is awesome.");
    }

    private long insertUser(Connection conn, String name, String email, String passwordHash, Role role) throws Exception {
        String sql = "INSERT INTO users (name, email, password_hash, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, passwordHash);
            ps.setString(4, role.name());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        }
        return -1;
    }

    private long insertProduct(Connection conn, long sellerId, String name, String desc, BigDecimal price, int stock, String category, String imageUrl) throws Exception {
        String sql = "INSERT INTO products (seller_id, name, description, price, stock, category, image_url) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, sellerId);
            ps.setString(2, name);
            ps.setString(3, desc);
            ps.setBigDecimal(4, price);
            ps.setInt(5, stock);
            ps.setString(6, category);
            ps.setString(7, imageUrl);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        }
        return -1;
    }

    private long insertOrder(Connection conn, long buyerId, BigDecimal total, String status) throws Exception {
        String sql = "INSERT INTO orders (buyer_id, total_amount, status) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, buyerId);
            ps.setBigDecimal(2, total);
            ps.setString(3, status);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        }
        return -1;
    }

    private void insertOrderItem(Connection conn, long orderId, long productId, int qty, BigDecimal unitPrice) throws Exception {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            ps.setLong(2, productId);
            ps.setInt(3, qty);
            ps.setBigDecimal(4, unitPrice);
            ps.executeUpdate();
        }
    }

    private void insertReview(Connection conn, long userId, long productId, int rating, String comment) throws Exception {
        String sql = "INSERT INTO reviews (user_id, product_id, rating, comment) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            ps.setInt(3, rating);
            ps.setString(4, comment);
            ps.executeUpdate();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("[Gokul Mart] Shutting down HikariCP connection pool...");
        DBUtil.closePool();
    }
}
