<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="/WEB-INF/jsp/common/header.jsp" %>
<%@ include file="/WEB-INF/jsp/common/navbar.jsp" %>

<style>
.detail-grid {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 3rem;
    align-items: start;
    margin-bottom: 4rem;
}
.detail-img-wrap {
    width: 100%;
    height: 420px;
    background: var(--surface);
    border: 1px solid var(--border);
    border-radius: var(--radius-xl);
    overflow: hidden;
}
.detail-img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}
.detail-info {
    display: flex;
    flex-direction: column;
}
.star-rating-input {
    display: flex;
    gap: 0.5rem;
    font-size: 1.8rem;
    color: var(--text-muted);
    cursor: pointer;
    margin: 0.5rem 0;
}
.star-icon {
    transition: color 0.2s ease;
}
.star-icon.selected, .star-icon:hover {
    color: var(--warning);
}
.review-item {
    background: var(--surface);
    border: 1px solid var(--border);
    border-radius: var(--radius-md);
    padding: 1.25rem;
    margin-bottom: 1rem;
}
</style>

<main class="main-content container">

    <c:if test="${not empty param.success}">
        <div class="alert alert-success">${param.success}</div>
    </c:if>
    <c:if test="${not empty param.error}">
        <div class="alert alert-danger">${param.error}</div>
    </c:if>

    <div class="detail-grid">
        <div class="detail-img-wrap">
            <img src="${product.imageUrl}" alt="${product.name}" class="detail-img" onerror="this.src='https://images.unsplash.com/photo-1560343090-f0409e92791a?w=600&auto=format&fit=crop'">
        </div>

        <div class="detail-info">
            <span class="card-category-badge" style="position: static; display: inline-block; width: fit-content; margin-bottom: 1rem;">
                ${product.category}
            </span>
            <h1 style="font-size: 2.2rem; font-weight: 800; line-height: 1.2; margin-bottom: 0.5rem;">${product.name}</h1>
            <p style="color: var(--text-secondary); margin-bottom: 1.5rem;">Sold by <strong style="color: var(--text-primary);">${product.sellerName}</strong></p>

            <div style="display: flex; align-items: baseline; gap: 1.5rem; margin-bottom: 1.5rem;">
                <div class="price-tag" style="font-size: 2.2rem;">&#8377;<fmt:formatNumber value="${product.price}" pattern="#,##0.00"/></div>
                <div class="stock-tag ${product.stock <= 5 ? (product.stock == 0 ? 'out' : 'low') : ''}" style="font-size: 1rem;">
                    <c:choose>
                        <c:when test="${product.stock > 0}">${product.stock} items available</c:when>
                        <c:otherwise>Currently Out of Stock</c:otherwise>
                    </c:choose>
                </div>
            </div>

            <div style="background: var(--surface); padding: 1.5rem; border-radius: var(--radius-lg); border: 1px solid var(--border); margin-bottom: 2rem;">
                <h4 style="font-size: 0.9rem; text-transform: uppercase; color: var(--text-secondary); margin-bottom: 0.5rem;">Product Description</h4>
                <p style="color: var(--text-primary); line-height: 1.7;">${product.description}</p>
            </div>

            <!-- Actions: Cart & Wishlist -->
            <div style="display: flex; flex-direction: column; gap: 1rem;">
                <c:choose>
                    <c:when test="${product.stock > 0}">
                        <form action="${pageContext.request.contextPath}/cart/add" method="POST" style="display: flex; gap: 1rem; align-items: center; margin: 0;">
                            <input type="hidden" name="productId" value="${product.id}">
                            <div style="width: 100px;">
                                <input type="number" name="quantity" value="1" min="1" max="${product.stock}" class="form-control" style="text-align: center; font-size: 1.1rem; font-weight: 700;">
                            </div>
                            <button type="submit" class="btn btn-accent btn-lg" style="flex: 1;">
                                Add to Shopping Cart &rarr;
                            </button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-outline btn-lg" disabled style="opacity: 0.5; width: 100%;">Out of Stock</button>
                    </c:otherwise>
                </c:choose>

                <!-- Wishlist Action -->
                <c:if test="${sessionScope.user != null and sessionScope.user.role == 'BUYER'}">
                    <c:choose>
                        <c:when test="${product.wishlisted}">
                            <form action="${pageContext.request.contextPath}/wishlist/remove" method="POST" style="margin: 0;">
                                <input type="hidden" name="productId" value="${product.id}">
                                <button type="submit" class="btn btn-outline btn-lg" style="width: 100%; color: red; border-color: red;">
                                    ♥ Remove from Wishlist
                                </button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <form action="${pageContext.request.contextPath}/wishlist/add" method="POST" style="margin: 0;">
                                <input type="hidden" name="productId" value="${product.id}">
                                <button type="submit" class="btn btn-outline btn-lg" style="width: 100%;">
                                    ♡ Add to Wishlist
                                </button>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </c:if>
            </div>
        </div>
    </div>

    <!-- Reviews & Rating Section -->
    <section style="border-top: 1px solid var(--border); padding-top: 3rem;">
        <div class="section-header">
            <div>
                <h2 class="section-title">Customer Reviews</h2>
                <p style="color: var(--text-secondary);">
                    Average Rating: <strong style="color: var(--warning); font-size: 1.1rem;">★ <fmt:formatNumber value="${avgRating}" pattern="0.0"/> / 5.0</strong> (${reviews.size()} reviews)
                </p>
            </div>
        </div>

        <!-- Add Review Form if Buyer has Purchased Product -->
        <c:if test="${canReview}">
            <div style="background: var(--surface); border: 1px solid var(--border); border-radius: var(--radius-lg); padding: 1.75rem; margin-bottom: 2.5rem;">
                <h3 style="font-size: 1.2rem; font-weight: 700; margin-bottom: 0.5rem;">Leave a Verified Review</h3>
                <form action="${pageContext.request.contextPath}/review/add" method="POST">
                    <input type="hidden" name="productId" value="${product.id}">
                    <input type="hidden" id="ratingInput" name="rating" value="5">

                    <div class="form-group">
                        <label class="form-label">Rating</label>
                        <div class="star-rating-input">
                            <span class="star-icon selected" data-value="1">★</span>
                            <span class="star-icon selected" data-value="2">★</span>
                            <span class="star-icon selected" data-value="3">★</span>
                            <span class="star-icon selected" data-value="4">★</span>
                            <span class="star-icon selected" data-value="5">★</span>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="comment">Your Comment</label>
                        <textarea id="comment" name="comment" class="form-control" placeholder="Share details of your experience with this product..." required></textarea>
                    </div>

                    <button type="submit" class="btn btn-primary btn-sm">Submit Review</button>
                </form>
            </div>
        </c:if>

        <!-- Existing Reviews List -->
        <c:choose>
            <c:when test="${not empty reviews}">
                <c:forEach var="r" items="${reviews}">
                    <div class="review-item">
                        <div style="display: flex; justify-content: space-between; margin-bottom: 0.5rem;">
                            <strong>${r.userName}</strong>
                            <span style="color: var(--warning); font-weight: 700;">
                                <c:forEach begin="1" end="${r.rating}">★</c:forEach>
                            </span>
                        </div>
                        <p style="color: var(--text-secondary); font-size: 0.95rem;">${r.comment}</p>
                        <small style="color: var(--text-muted); display: block; margin-top: 0.5rem;">Posted on ${r.createdAt}</small>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <p style="color: var(--text-muted); font-style: italic;">No reviews posted yet for this product.</p>
            </c:otherwise>
        </c:choose>
    </section>

</main>

<%@ include file="/WEB-INF/jsp/common/footer.jsp" %>
