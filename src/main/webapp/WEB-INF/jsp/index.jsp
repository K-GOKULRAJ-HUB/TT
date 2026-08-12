<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="/WEB-INF/jsp/common/header.jsp" %>
<%@ include file="/WEB-INF/jsp/common/navbar.jsp" %>

<main class="main-content container">

    <c:if test="${not empty param.success}">
        <div class="alert alert-success">${param.success}</div>
    </c:if>
    <c:if test="${not empty param.error}">
        <div class="alert alert-danger">${param.error}</div>
    </c:if>

    <!-- Hero Section -->
    <section class="hero">
        <h1 class="hero-title">
            Discover products from <span>trusted sellers</span>, all in one marketplace.
        </h1>
        <p class="hero-subtitle">
            Gokul Mart connects quality sellers with smart buyers. Browse thousands of top products with secure checkout.
        </p>

        <!-- Search Bar -->
        <div class="search-container">
            <form action="${pageContext.request.contextPath}/products" method="GET" class="search-form">
                <input type="text" name="query" class="search-input" placeholder="Search for products, brands, or categories..." required>
                <select name="category" class="category-select">
                    <option value="ALL">All Categories</option>
                    <option value="Accessories">Accessories</option>
                    <option value="Books">Books</option>
                    <option value="Clothing">Clothing</option>
                    <option value="Electronics">Electronics</option>
                    <option value="Home">Home</option>
                </select>
                <button type="submit" class="btn btn-accent">Search</button>
            </form>
        </div>
    </section>

    <!-- Category Chips -->
    <div class="category-chips">
        <a href="${pageContext.request.contextPath}/products?category=ALL" class="chip active">All Products</a>
        <a href="${pageContext.request.contextPath}/products?category=Accessories" class="chip">Accessories</a>
        <a href="${pageContext.request.contextPath}/products?category=Books" class="chip">Books</a>
        <a href="${pageContext.request.contextPath}/products?category=Clothing" class="chip">Clothing</a>
        <a href="${pageContext.request.contextPath}/products?category=Electronics" class="chip">Electronics</a>
        <a href="${pageContext.request.contextPath}/products?category=Home" class="chip">Home</a>
    </div>

    <!-- Featured Products -->
    <section>
        <div class="section-header">
            <h2 class="section-title">Featured Marketplace Listings</h2>
            <a href="${pageContext.request.contextPath}/products" class="btn btn-outline btn-sm">Explore All &rarr;</a>
        </div>

        <div class="product-grid">
            <c:forEach var="p" items="${featuredProducts}">
                <div class="product-card">
                    <div class="card-img-wrap">
                        <span class="card-category-badge">${p.category}</span>
                        <img src="${p.imageUrl}" alt="${p.name}" class="card-img" onerror="this.src='https://images.unsplash.com/photo-1560343090-f0409e92791a?w=600&auto=format&fit=crop'">
                    </div>
                    <div class="card-body">
                        <h3 class="card-title">${p.name}</h3>
                        <p class="card-seller">Sold by <strong>${p.sellerName}</strong></p>

                        <div class="card-footer">
                            <div>
                                <div class="price-tag">&#8377;<fmt:formatNumber value="${p.price}" pattern="#,##0.00"/></div>
                                <div class="stock-tag ${p.stock <= 5 ? (p.stock == 0 ? 'out' : 'low') : ''}">
                                    <c:choose>
                                        <c:when test="${p.stock > 0}">${p.stock} in stock</c:when>
                                        <c:otherwise>Out of stock</c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                            <a href="${pageContext.request.contextPath}/product/detail?id=${p.id}" class="btn btn-primary btn-sm">View</a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </section>

</main>

<%@ include file="/WEB-INF/jsp/common/footer.jsp" %>
