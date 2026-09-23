<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="/WEB-INF/jsp/common/header.jsp" %>
<%@ include file="/WEB-INF/jsp/common/navbar.jsp" %>

<main class="main-content container">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;">
        <h1 style="font-size: 2rem; font-weight: 800;">My Wishlist</h1>
    </div>

    <c:if test="${empty wishlist}">
        <div class="empty-state">
            <h3>Your wishlist is empty</h3>
            <p>Looks like you haven't added any products to your wishlist yet.</p>
            <a href="${pageContext.request.contextPath}/products" class="btn btn-primary" style="margin-top: 1rem;">Browse Products</a>
        </div>
    </c:if>

    <c:if test="${not empty wishlist}">
        <div class="product-grid">
            <c:forEach var="product" items="${wishlist}">
                <div class="product-card">
                    <img src="${product.imageUrl}" alt="${product.name}" class="product-img">
                    <div class="product-info">
                        <div style="font-size: 0.8rem; color: var(--primary); font-weight: 700; margin-bottom: 0.5rem; text-transform: uppercase;">
                            ${product.category}
                        </div>
                        <h3 class="product-title">${product.name}</h3>
                        <p class="product-seller">By ${product.sellerName}</p>
                        <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 1rem;">
                            <span class="product-price">₹${product.price}</span>
                            <div style="display: flex; gap: 0.5rem;">
                                <!-- Remove from Wishlist Form -->
                                <form action="${pageContext.request.contextPath}/wishlist/remove" method="POST" style="margin: 0;">
                                    <input type="hidden" name="productId" value="${product.id}">
                                    <button type="submit" class="btn btn-outline" style="color: red; border-color: red;" title="Remove from Wishlist">♥ Remove</button>
                                </form>
                                <form action="${pageContext.request.contextPath}/cart/add" method="POST" style="margin: 0;">
                                    <input type="hidden" name="productId" value="${product.id}">
                                    <input type="hidden" name="quantity" value="1">
                                    <button type="submit" class="btn btn-primary" title="Add to Cart">Add to Cart</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:if>
</main>

<%@ include file="/WEB-INF/jsp/common/footer.jsp" %>
