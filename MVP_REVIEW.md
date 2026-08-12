# Gokul Mart — Capstone MVP Review Guide

## 1. Executive Summary

**Gokul Mart** is a functional, stable, multi-seller e-commerce marketplace built as a college capstone project. The application connects buyers, sellers, and administrators on a single digital platform.

---

## 2. Implemented MVP Scope

- **Layered Enterprise Architecture**:
  - `Controller` (Servlets handling HTTP requests and forwarding to JSP)
  - `Service` (Business rules, stock validation, total calculation)
  - `DAO` (JDBC Data Access Objects)
  - `Database` (HikariCP Connection Pool & H2 Database Engine)

- **Security & Authorization**:
  - jBCrypt password hashing (zero plain-text passwords stored).
  - Session-based authentication with `AuthFilter` enforcing role-based access (`BUYER`, `SELLER`, `ADMIN`).
  - `PreparedStatement` usage preventing SQL Injection vulnerabilities.

- **Transaction-Safe Checkout**:
  - Atomic database transactions using JDBC `setAutoCommit(false)`, `commit()`, and `rollback()`.
  - Concurrency-safe stock updates.

- **Design System**:
  - Custom Vanilla CSS **Midnight Marketplace** theme (`#0B1020` background, `#7C3AED` violet, `#F97316` orange accents).

---

## 3. Demo Account Credentials

| Role | Email | Password | Purpose |
| :--- | :--- | :--- | :--- |
| **BUYER** | `buyer1@gokulmart.com` | `buyer123` | Demo buyer account (Gokulraj) |
| **BUYER** | `buyer2@gokulmart.com` | `buyer123` | Demo buyer account (Ananya Sharma) |
| **SELLER** | `seller1@gokulmart.com` | `seller123` | Electronics & Accessories seller (TechGear) |
| **SELLER** | `seller2@gokulmart.com` | `seller123` | Books & Home seller (Apex) |
| **ADMIN** | `admin@gokulmart.com` | `admin123` | Platform Administrator |

---

## 4. Step-by-Step Viva Demo Script

1. **Launch App**: Open `http://localhost:8080/gokulmart` in the browser.
2. **Homepage**: Show hero section, brand logo, search bar, and featured product grid.
3. **Category Filter**: Click on **Electronics** chip to filter listings.
4. **Search**: Search for `"Earbuds"` in the search bar.
5. **Product Detail**: Click **View Details** on *SoundPulse Pro Wireless Earbuds*. Highlight description, stock level, seller name, and customer reviews.
6. **Buyer Authentication**: Click **Login** and sign in as `buyer1@gokulmart.com` / `buyer123`.
7. **Add to Cart**: Click **Add to Shopping Cart** (quantity: 2).
8. **Cart Management**: View shopping cart. Adjust quantity or verify subtotal calculation.
9. **Checkout**: Click **Proceed to Checkout**.
10. **Place Order**: Click **Confirm & Place Order**. Explain how JDBC transaction atomically deducts stock, creates order items, and clears the cart.
11. **Order History & Invoice**: View order invoice showing status `PENDING`.
12. **Review Product**: Return to product detail page and post a 5-star review.
13. **Logout**: Log out from buyer account.
14. **Seller Login**: Log in as `seller1@gokulmart.com` / `seller123`.
15. **Seller Dashboard**: Demonstrate seller metrics (total listings, stock count, customer orders).
16. **Product CRUD**: Add a new product or edit price/stock of an existing listing.
17. **Seller Fulfillment**: Navigate to **Manage Orders** and update order status from `PENDING` to `SHIPPED`.
18. **Admin Login**: Log out and log in as `admin@gokulmart.com` / `admin123`.
19. **Admin Oversight**: View system statistics, user management list, all marketplace listings, and global order status overrides.

---

## 5. Architecture Viva Talking Points

- **Q: Why Java Servlets and JSP instead of Spring Boot?**
  - *A*: Servlets and JSP provide direct, transparent understanding of the Java Web Spec, HTTP Request/Response lifecycle, Session handling, and Filter chaining without framework black boxes.

- **Q: How is database connection leakage prevented?**
  - *A*: By using HikariCP connection pooling combined with Java try-with-resources blocks ensuring all connections, prepared statements, and result sets are closed automatically.

- **Q: How does checkout ensure atomic consistency?**
  - *A*: Checkout initiates a JDBC transaction (`conn.setAutoCommit(false)`). If stock validation fails for any item, `conn.rollback()` restores state; otherwise `conn.commit()` completes the order atomically.

---

## 6. Known MVP Scope Boundaries & Future Scope

- **Payment Gateway**: Uses mock checkout simulation; real Razorpay/Stripe integration can be added.
- **Notifications**: Email/SMS notifications logged to console; live SMTP integration is future scope.
- **Analytics**: Basic counts implemented; graphic charts can be added.
