# Gokul Mart — Multi-Seller E-Commerce Marketplace

**College Capstone MVP**

Gokul Mart is a centralized multi-seller e-commerce marketplace web application designed to connect independent product sellers with buyers. Built with Java 17, Java Servlets, JDBC, HikariCP, H2 Database, JSP/JSTL, and Vanilla CSS under the modern **Midnight Marketplace** dark theme.

---

## 1. Problem Statement

Small and independent sellers often need a common digital platform to list and manage their products, while customers need a convenient way to discover, compare and purchase products. Gokul Mart solves this problem by providing a centralized multi-seller marketplace where sellers can manage product listings and buyers can browse products, add products to a cart and place orders.

---

## 2. Objective

To build a stable, attractive, and functional college capstone MVP that demonstrates the complete basic marketplace workflow across three user roles: **BUYER**, **SELLER**, and **ADMIN**.

---

## 3. Key Features

- **Buyer Workflow**:
  - Account registration & BCrypt login authentication.
  - Browse featured products and search by keyword or category.
  - Filter across 5 categories: *Accessories*, *Books*, *Clothing*, *Electronics*, *Home*.
  - View detailed product specifications, stock levels, and seller information.
  - Interactive shopping cart with live quantity updates and stock limit enforcement.
  - Atomic transaction checkout with stock deduction and order confirmation.
  - View order history and detailed invoices.
  - Submit 1–5 star ratings and reviews for purchased products.

- **Seller Workflow**:
  - Secure seller authentication & access to Seller Control Panel.
  - Create, update, and delete product listings.
  - Inventory & stock management.
  - Order fulfillment dashboard with real-time status transitions (*PENDING*, *CONFIRMED*, *SHIPPED*, *DELIVERED*, *CANCELLED*).

- **Admin Workflow**:
  - System overview dashboard showing platform user metrics, active listings, and total orders.
  - User management oversight (Buyers, Sellers, Admins).
  - Marketplace listing moderation (ability to delete non-compliant listings).
  - Global order status tracking & override management.

---

## 4. Tech Stack

- **Frontend**: JSP (JavaServer Pages), JSTL 1.2, HTML5, Vanilla CSS3 (Midnight Marketplace Theme), Vanilla JavaScript.
- **Backend**: Java 17, Java Servlets 4.0, Layered Architecture (`controller` -> `service` -> `dao` -> `model`).
- **Database**: H2 Database (Embedded Mode), JDBC, HikariCP Connection Pool.
- **Security**: jBCrypt password hashing, session authentication, AuthFilter role guard, PreparedStatement SQL protection.
- **Build Tool**: Apache Maven (WAR packaging).
- **Application Server**: Apache Tomcat 9.
- **Containerization**: Docker & Docker Compose.

---

## 5. System Architecture

```
                       GOKUL MART
                            |
             ┌──────────────┼──────────────┐
             ↓              ↓              ↓
           BUYER          ADMIN          SELLER
             |
             ↓
         FRONTEND
      JSP / HTML / CSS / JS
             |
             ↓
         SERVLETS
        Controllers
             |
             ↓
          SERVICES
       Business Logic
             |
             ↓
            DAO
         JDBC / SQL
             |
             ↓
         HikariCP
             |
             ↓
         H2 DATABASE
```

---

## 6. Project Structure

```
d:/capstone/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/gokulmart/
│   │   │       ├── controller/   # Servlets
│   │   │       ├── dao/          # JDBC DAOs
│   │   │       ├── filter/       # AuthFilter
│   │   │       ├── listener/     # DBInitListener
│   │   │       ├── model/        # Domain Entities & Enums
│   │   │       ├── service/      # Business Logic Services
│   │   │       └── util/         # HikariCP & jBCrypt Utilities
│   │   ├── resources/
│   │   │   ├── schema.sql        # Database DDL
│   │   │   └── seed.sql          # Seed Data Reference
│   │   └── webapp/
│   │       ├── css/style.css     # Midnight Marketplace Design System
│   │       ├── js/main.js        # Interactive Vanilla JS
│   │       └── WEB-INF/
│   │           ├── web.xml       # Servlet Mapping & Filters
│   │           └── jsp/          # JSP Views & Fragments
│   └── test/
│       └── java/com/gokulmart/   # JUnit 5 Unit Tests
├── Dockerfile
├── docker-compose.yml
├── pom.xml
├── README.md
└── MVP_REVIEW.md
```

---

## 7. Database Schema

- `users`: `id`, `name`, `email`, `password_hash`, `role`, `created_at`
- `products`: `id`, `seller_id`, `name`, `description`, `price`, `stock`, `category`, `image_url`, `created_at`
- `cart_items`: `id`, `user_id`, `product_id`, `quantity`
- `orders`: `id`, `buyer_id`, `total_amount`, `status`, `created_at`
- `order_items`: `id`, `order_id`, `product_id`, `quantity`, `unit_price`
- `reviews`: `id`, `user_id`, `product_id`, `rating`, `comment`, `created_at`

---

## 8. Demo Credentials

The database is automatically initialized and seeded on first application startup.

| Role | Email | Password | Description |
| :--- | :--- | :--- | :--- |
| **ADMIN** | `admin@gokulmart.com` | `admin123` | Platform Administrator |
| **SELLER** | `seller1@gokulmart.com` | `seller123` | TechGear Official Seller |
| **SELLER** | `seller2@gokulmart.com` | `seller123` | Apex Publishers & Wear Seller |
| **BUYER** | `buyer1@gokulmart.com` | `buyer123` | Gokulraj (Sample Buyer) |
| **BUYER** | `buyer2@gokulmart.com` | `buyer23` | Ananya Sharma (Sample Buyer) |

---

## 9. How to Build & Test

### Maven Build
```bash
mvn clean package
```

### Run Unit Tests
```bash
mvn clean verify
```

---

## 10. How to Run Locally

### Option A: Using Tomcat Server
1. Build the project WAR: `mvn clean package`
2. Deploy `target/gokulmart.war` to Apache Tomcat 9 `webapps/` folder.
3. Access at: `http://localhost:8080/gokulmart/`

### Option B: Using Docker Compose
```bash
docker-compose up --build
```
Access at: `http://localhost:8080/`

---

## 11. Render Deployment Instructions

1. Push this repository to GitHub.
2. Create a new **Web Service** on [Render](https://render.com).
3. Select **Docker** environment.
4. Render automatically reads the included `Dockerfile`, builds the WAR using Maven, and runs Apache Tomcat.
5. Render sets a dynamic `$PORT` environment variable, which our `Dockerfile` automatically injects into Tomcat's `server.xml`.

---

## 12. Future Enhancements

- Integration with real Payment Gateways (Razorpay/Stripe).
- Real-time order tracking & SMS/Email notifications.
- Seller payout & revenue analytics charts.
- Wishlist & product recommendations.
