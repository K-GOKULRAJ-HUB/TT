<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="/WEB-INF/jsp/common/header.jsp" %>
<%@ include file="/WEB-INF/jsp/common/navbar.jsp" %>

<main class="main-content container">

    <div class="section-header">
        <div>
            <h1 class="section-title">Marketplace Products</h1>
            <p style="color: var(--text-secondary); font-size: 0.95rem;">
                <c:choose>
                    <c:when test="${not empty query}">Results for "${query}"</c:when>
                    <c:otherwise>Explore quality items across all 5 categories</c:otherwise>
                </c:choose>
            </p>
        </div>
    </div>
    
    <c:if test="${not empty param.success}">
        <div class="alert alert-success" style="margin-bottom: 1rem;">${param.success}</div>
    </c:if>
    <c:if test="${not empty param.error}">
        <div class="alert alert-danger" style="margin-bottom: 1rem;">${param.error}</div>
    </c:if>

    <!-- Category Chips -->
    <div class="category-chips" style="justify-content: flex-start;">
        <a href="${pageContext.request.contextPath}/products?category=ALL&query=${query}" class="chip ${selectedCategory eq 'ALL' ? 'active' : ''}">All</a>
        <a href="${pageContext.request.contextPath}/products?category=Accessories&query=${query}" class="chip ${selectedCategory eq 'Accessories' ? 'active' : ''}">Accessories</a>
        <a href="${pageContext.request.contextPath}/products?category=Books&query=${query}" class="chip ${selectedCategory eq 'Books' ? 'active' : ''}">Books</a>
        <a href="${pageContext.request.contextPath}/products?category=Clothing&query=${query}" class="chip ${selectedCategory eq 'Clothing' ? 'active' : ''}">Clothing</a>
        <a href="${pageContext.request.contextPath}/products?category=Electronics&query=${query}" class="chip ${selectedCategory eq 'Electronics' ? 'active' : ''}">Electronics</a>
        <a href="${pageContext.request.contextPath}/products?category=Home&query=${query}" class="chip ${selectedCategory eq 'Home' ? 'active' : ''}">Home</a>
    </div>

    <!-- Product Grid -->
    <c:choose>
        <c:when test="${not empty products}">
            <div class="product-grid">
                <c:forEach var="p" items="${products}">
                    <div class="product-card">
                        <div class="card-img-wrap">
                            <span class="card-category-badge">${p.category}</span>
                            <img src="${p.imageUrl}" alt="${p.name}" class="card-img" onerror="this.src='https://images.unsplash.com/photo-1560343090-f0409e92791a?w=600&auto=format&fit=crop'">
                        </div>
                        <div class="card-body">
                            <h3 class="card-title">${p.name}</h3>
                            <p class="card-seller">Seller: <strong>${p.sellerName}</strong></p>

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
                                <div style="display: flex; gap: 0.5rem; align-items: center;">
                                    <c:if test="${sessionScope.user != null and sessionScope.user.role == 'BUYER'}">
                                        <c:choose>
                                            <c:when test="${p.wishlisted}">
                                                <form action="${pageContext.request.contextPath}/wishlist/remove" method="POST" style="margin: 0;">
                                                    <input type="hidden" name="productId" value="${p.id}">
                                                    <button type="submit" class="btn btn-outline btn-sm" style="color: red; border-color: red;" title="Remove from Wishlist">♥</button>
                                                </form>
                                            </c:when>
                                            <c:otherwise>
                                                <form action="${pageContext.request.contextPath}/wishlist/add" method="POST" style="margin: 0;">
                                                    <input type="hidden" name="productId" value="${p.id}">
                                                    <button type="submit" class="btn btn-outline btn-sm" title="Add to Wishlist">♡</button>
                                                </form>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>
                                    <a href="${pageContext.request.contextPath}/product/detail?id=${p.id}" class="btn btn-primary btn-sm">View Details</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <div style="text-align: center; padding: 4rem 1rem; background: var(--surface); border-radius: var(--radius-xl); border: 1px solid var(--border);">
                <h3 style="font-size: 1.5rem; color: var(--text-secondary); margin-bottom: 0.5rem;">No products match your filter</h3>
                <p style="color: var(--text-muted); margin-bottom: 1.5rem;">Try searching for another keyword or selecting a different category.</p>
                <a href="${pageContext.request.contextPath}/products" class="btn btn-outline">Clear Filters</a>
            </div>
        </c:otherwise>
    </c:choose>

</main>

<%@ include file="/WEB-INF/jsp/common/footer.jsp" %>
