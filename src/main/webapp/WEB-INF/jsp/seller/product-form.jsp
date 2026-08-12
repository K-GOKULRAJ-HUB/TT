<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="/WEB-INF/jsp/common/header.jsp" %>
<%@ include file="/WEB-INF/jsp/common/navbar.jsp" %>

<main class="main-content container">

    <div class="form-card" style="max-width: 650px;">
        <div style="margin-bottom: 2rem;">
            <h2 style="font-size: 1.8rem; font-weight: 800; margin-bottom: 0.5rem;">
                <c:choose>
                    <c:when test="${not empty product}">Edit Product Listing</c:when>
                    <c:otherwise>Create New Product Listing</c:otherwise>
                </c:choose>
            </h2>
            <p style="color: var(--text-secondary); font-size: 0.95rem;">Enter product details for marketplace visibility</p>
        </div>

        <c:if test="${not empty param.error}">
            <div class="alert alert-danger">${param.error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/seller/product/${not empty product ? 'edit' : 'create'}" method="POST">
            <c:if test="${not empty product}">
                <input type="hidden" name="id" value="${product.id}">
            </c:if>

            <div class="form-group">
                <label class="form-label" for="name">Product Title</label>
                <input type="text" id="name" name="name" class="form-control" value="${product.name}" placeholder="e.g. Titan Steel Chronograph Watch" required>
            </div>

            <div class="form-group">
                <label class="form-label" for="category">Category</label>
                <select id="category" name="category" class="form-control" required>
                    <option value="Accessories" ${product.category eq 'Accessories' ? 'selected' : ''}>Accessories</option>
                    <option value="Books" ${product.category eq 'Books' ? 'selected' : ''}>Books</option>
                    <option value="Clothing" ${product.category eq 'Clothing' ? 'selected' : ''}>Clothing</option>
                    <option value="Electronics" ${product.category eq 'Electronics' ? 'selected' : ''}>Electronics</option>
                    <option value="Home" ${product.category eq 'Home' ? 'selected' : ''}>Home</option>
                </select>
            </div>

            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                <div class="form-group">
                    <label class="form-label" for="price">Price (&#8377;)</label>
                    <input type="number" step="0.01" id="price" name="price" class="form-control" value="${product.price}" placeholder="2499.00" required min="1">
                </div>

                <div class="form-group">
                    <label class="form-label" for="stock">Available Stock</label>
                    <input type="number" id="stock" name="stock" class="form-control" value="${product.stock}" placeholder="25" required min="0">
                </div>
            </div>

            <div class="form-group">
                <label class="form-label" for="imageUrl">Image URL</label>
                <input type="url" id="imageUrl" name="imageUrl" class="form-control" value="${product.imageUrl}" placeholder="https://images.unsplash.com/..." required>
            </div>

            <div class="form-group">
                <label class="form-label" for="description">Detailed Description</label>
                <textarea id="description" name="description" class="form-control" placeholder="Describe product features, specs, and highlights..." required>${product.description}</textarea>
            </div>

            <div style="display: flex; gap: 1rem; margin-top: 2rem;">
                <button type="submit" class="btn btn-accent" style="flex: 1;">Save Listing &rarr;</button>
                <a href="${pageContext.request.contextPath}/seller/dashboard" class="btn btn-outline">Cancel</a>
            </div>
        </form>
    </div>

</main>

<%@ include file="/WEB-INF/jsp/common/footer.jsp" %>
