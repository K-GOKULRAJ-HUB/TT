<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="/WEB-INF/jsp/common/header.jsp" %>
<%@ include file="/WEB-INF/jsp/common/navbar.jsp" %>

<main class="main-content container">

    <div class="section-header">
        <div>
            <h1 class="section-title">Administrator Control Center</h1>
            <p style="color: var(--text-secondary);">Marketplace overview, user management, and global listing controls</p>
        </div>
    </div>

    <!-- Metrics Cards -->
    <div class="stats-grid">
        <div class="stat-card">
            <span class="stat-label">Total Platform Users</span>
            <span class="stat-val">${totalUsers}</span>
            <div style="font-size: 0.8rem; color: var(--text-muted); margin-top: 0.25rem;">
                ${buyerCount} Buyers • ${sellerCount} Sellers
            </div>
        </div>
        <div class="stat-card">
            <span class="stat-label">Total Product Listings</span>
            <span class="stat-val" style="color: var(--accent);">${totalProducts}</span>
        </div>
        <div class="stat-card">
            <span class="stat-label">Marketplace Orders</span>
            <span class="stat-val" style="color: var(--primary-hover);">${totalOrders}</span>
        </div>
    </div>

    <!-- Recent System Activity -->
    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 2rem;">
        <div>
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem;">
                <h3 style="font-size: 1.2rem; font-weight: 700;">Recent User Registrations</h3>
                <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-outline btn-sm">View All &rarr;</a>
            </div>
            <div class="table-responsive">
                <table class="custom-table">
                    <thead>
                        <tr>
                            <th>User Name</th>
                            <th>Email</th>
                            <th>Role</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="u" items="${recentUsers}">
                            <tr>
                                <td><strong>${u.name}</strong></td>
                                <td style="font-size: 0.85rem; color: var(--text-secondary);">${u.email}</td>
                                <td><span class="role-tag ${u.role.name().toLowerCase()}">${u.role}</span></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <div>
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem;">
                <h3 style="font-size: 1.2rem; font-weight: 700;">Recent Marketplace Orders</h3>
                <a href="${pageContext.request.contextPath}/admin/orders" class="btn btn-outline btn-sm">View All &rarr;</a>
            </div>
            <div class="table-responsive">
                <table class="custom-table">
                    <thead>
                        <tr>
                            <th>Order ID</th>
                            <th>Buyer</th>
                            <th>Amount</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="o" items="${recentOrders}">
                            <tr>
                                <td><strong>#${o.id}</strong></td>
                                <td>${o.buyerName}</td>
                                <td style="font-weight: 700;">&#8377;<fmt:formatNumber value="${o.totalAmount}" pattern="#,##0.00"/></td>
                                <td><span class="badge badge-${o.status.name().toLowerCase()}">${o.status}</span></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

</main>

<%@ include file="/WEB-INF/jsp/common/footer.jsp" %>
