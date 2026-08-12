<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="/WEB-INF/jsp/common/header.jsp" %>
<%@ include file="/WEB-INF/jsp/common/navbar.jsp" %>

<main class="main-content container">

    <div class="section-header">
        <div>
            <h1 class="section-title">Seller Control Panel</h1>
            <p style="color: var(--text-secondary);">Manage your product inventory, stock levels, and store orders</p>
        </div>
        <a href="${pageContext.request.contextPath}/seller/product/create" class="btn btn-accent">+ Create New Product</a>
    </div>

    <c:if test="${not empty param.success}">
        <div class="alert alert-success">${param.success}</div>
    </c:if>
    <c:if test="${not empty param.error}">
        <div class="alert alert-danger">${param.error}</div>
    </c:if>

    <!-- Stats Grid -->
    <div class="stats-grid">
        <div class="stat-card">
            <span class="stat-label">Total Listings</span>
            <span class="stat-val">${totalProductsCount}</span>
        </div>
        <div class="stat-card">
            <span class="stat-label">Total Stock Available</span>
            <span class="stat-val" style="color: var(--success);">${totalStock}</span>
        </div>
        <div class="stat-card">
            <span class="stat-label">Customer Orders</span>
            <span class="stat-val" style="color: var(--primary-hover);">${totalOrdersCount}</span>
        </div>
    </div>

    <!-- Product Management Table -->
    <section>
        <h2 style="font-size: 1.4rem; font-weight: 700; margin-bottom: 1rem;">My Product Listings</h2>

        <c:choose>
            <c:when test="${not empty products}">
                <div class="table-responsive">
                    <table class="custom-table">
                        <thead>
                            <tr>
                                <th>Product</th>
                                <th>Category</th>
                                <th>Price</th>
                                <th>Stock Level</th>
                                <th>Created Date</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="p" items="${products}">
                                <tr>
                                    <td>
                                        <div style="display: flex; gap: 1rem; align-items: center;">
                                            <img src="${p.imageUrl}" alt="${p.name}" style="width: 48px; height: 48px; object-fit: cover; border-radius: var(--radius-md);" onerror="this.src='https://images.unsplash.com/photo-1560343090-f0409e92791a?w=600&auto=format&fit=crop'">
                                            <div>
                                                <strong>${p.name}</strong>
                                            </div>
                                        </div>
                                    </td>
                                    <td><span class="card-category-badge" style="position: static;">${p.category}</span></td>
                                    <td style="font-weight: 700;">&#8377;<fmt:formatNumber value="${p.price}" pattern="#,##0.00"/></td>
                                    <td>
                                        <span class="stock-tag ${p.stock <= 5 ? (p.stock == 0 ? 'out' : 'low') : ''}">
                                            ${p.stock} units
                                        </span>
                                    </td>
                                    <td>${p.createdAt}</td>
                                    <td>
                                        <div style="display: flex; gap: 0.5rem;">
                                            <a href="${pageContext.request.contextPath}/seller/product/edit?id=${p.id}" class="btn btn-outline btn-sm">Edit</a>
                                            <form action="${pageContext.request.contextPath}/seller/product/delete" method="POST" onsubmit="return confirm('Are you sure you want to delete this listing?')">
                                                <input type="hidden" name="id" value="${p.id}">
                                                <button type="submit" class="btn btn-danger btn-sm">Delete</button>
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div style="text-align: center; padding: 3rem 1rem; background: var(--surface); border-radius: var(--radius-lg); border: 1px solid var(--border);">
                    <p style="color: var(--text-muted); margin-bottom: 1rem;">You haven't listed any products yet.</p>
                    <a href="${pageContext.request.contextPath}/seller/product/create" class="btn btn-accent btn-sm">Add First Product</a>
                </div>
            </c:otherwise>
        </c:choose>
    </section>

</main>

<%@ include file="/WEB-INF/jsp/common/footer.jsp" %>
