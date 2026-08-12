<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="/WEB-INF/jsp/common/header.jsp" %>
<%@ include file="/WEB-INF/jsp/common/navbar.jsp" %>

<main class="main-content container">

    <div class="section-header">
        <div>
            <h1 class="section-title">All Marketplace Listings</h1>
            <p style="color: var(--text-secondary);">Administrative oversight of products listed across all seller accounts</p>
        </div>
    </div>

    <c:if test="${not empty param.success}">
        <div class="alert alert-success">${param.success}</div>
    </c:if>

    <div class="table-responsive">
        <table class="custom-table">
            <thead>
                <tr>
                    <th>Product</th>
                    <th>Seller</th>
                    <th>Category</th>
                    <th>Price</th>
                    <th>Stock</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="p" items="${products}">
                    <tr>
                        <td>
                            <div style="display: flex; gap: 1rem; align-items: center;">
                                <img src="${p.imageUrl}" alt="${p.name}" style="width: 44px; height: 44px; object-fit: cover; border-radius: var(--radius-md);" onerror="this.src='https://images.unsplash.com/photo-1560343090-f0409e92791a?w=600&auto=format&fit=crop'">
                                <div>
                                    <a href="${pageContext.request.contextPath}/product/detail?id=${p.id}" style="font-weight: 700;">${p.name}</a>
                                </div>
                            </div>
                        </td>
                        <td><strong>${p.sellerName}</strong></td>
                        <td><span class="card-category-badge" style="position: static;">${p.category}</span></td>
                        <td style="font-weight: 700;">&#8377;<fmt:formatNumber value="${p.price}" pattern="#,##0.00"/></td>
                        <td>${p.stock} units</td>
                        <td>
                            <form action="${pageContext.request.contextPath}/admin/product/delete" method="POST" onsubmit="return confirm('Remove this listing from the marketplace?')">
                                <input type="hidden" name="id" value="${p.id}">
                                <button type="submit" class="btn btn-danger btn-sm">Remove Listing</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

</main>

<%@ include file="/WEB-INF/jsp/common/footer.jsp" %>
