<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="/WEB-INF/jsp/common/header.jsp" %>
<%@ include file="/WEB-INF/jsp/common/navbar.jsp" %>

<main class="main-content container">

    <div class="section-header">
        <div>
            <h1 class="section-title">All Marketplace Orders</h1>
            <p style="color: var(--text-secondary);">Marketplace-wide order history and status overrides</p>
        </div>
    </div>

    <c:if test="${not empty param.success}">
        <div class="alert alert-success">${param.success}</div>
    </c:if>

    <div class="table-responsive">
        <table class="custom-table">
            <thead>
                <tr>
                    <th>Order ID</th>
                    <th>Buyer</th>
                    <th>Date</th>
                    <th>Items</th>
                    <th>Total</th>
                    <th>Status</th>
                    <th>Override Status</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="o" items="${orders}">
                    <tr>
                        <td style="font-weight: 700;">#${o.id}</td>
                        <td>
                            <strong>${o.buyerName}</strong>
                            <div style="font-size: 0.8rem; color: var(--text-muted);">${o.buyerEmail}</div>
                        </td>
                        <td>${o.createdAt}</td>
                        <td>${o.items.size()} item(s)</td>
                        <td style="font-weight: 700; color: var(--primary-hover);">&#8377;<fmt:formatNumber value="${o.totalAmount}" pattern="#,##0.00"/></td>
                        <td><span class="badge badge-${o.status.name().toLowerCase()}">${o.status}</span></td>
                        <td>
                            <form action="${pageContext.request.contextPath}/admin/order/update" method="POST" style="display: flex; gap: 0.5rem;">
                                <input type="hidden" name="orderId" value="${o.id}">
                                <select name="status" class="category-select" style="padding: 0.25rem 0.5rem; font-size: 0.85rem;">
                                    <option value="PENDING" ${o.status eq 'PENDING' ? 'selected' : ''}>PENDING</option>
                                    <option value="CONFIRMED" ${o.status eq 'CONFIRMED' ? 'selected' : ''}>CONFIRMED</option>
                                    <option value="SHIPPED" ${o.status eq 'SHIPPED' ? 'selected' : ''}>SHIPPED</option>
                                    <option value="DELIVERED" ${o.status eq 'DELIVERED' ? 'selected' : ''}>DELIVERED</option>
                                    <option value="CANCELLED" ${o.status eq 'CANCELLED' ? 'selected' : ''}>CANCELLED</option>
                                </select>
                                <button type="submit" class="btn btn-outline btn-sm">Save</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

</main>

<%@ include file="/WEB-INF/jsp/common/footer.jsp" %>
