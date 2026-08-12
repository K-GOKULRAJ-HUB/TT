<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="/WEB-INF/jsp/common/header.jsp" %>
<%@ include file="/WEB-INF/jsp/common/navbar.jsp" %>

<main class="main-content container">

    <div class="section-header">
        <div>
            <h1 class="section-title">Seller Order Fulfillment</h1>
            <p style="color: var(--text-secondary);">Track and update shipping status for customer orders</p>
        </div>
    </div>

    <c:if test="${not empty param.success}">
        <div class="alert alert-success">${param.success}</div>
    </c:if>

    <c:choose>
        <c:when test="${not empty orders}">
            <div class="table-responsive">
                <table class="custom-table">
                    <thead>
                        <tr>
                            <th>Order ID</th>
                            <th>Buyer Details</th>
                            <th>Items Sold</th>
                            <th>Order Date</th>
                            <th>Status</th>
                            <th>Update Status</th>
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
                                <td>
                                    <c:forEach var="item" items="${o.items}">
                                        <div style="font-size: 0.9rem;">
                                            • ${item.product.name} (Qty: ${item.quantity})
                                        </div>
                                    </c:forEach>
                                </td>
                                <td>${o.createdAt}</td>
                                <td><span class="badge badge-${o.status.name().toLowerCase()}">${o.status}</span></td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/seller/order/update" method="POST" style="display: flex; gap: 0.5rem;">
                                        <input type="hidden" name="orderId" value="${o.id}">
                                        <select name="status" class="category-select" style="padding: 0.25rem 0.5rem; font-size: 0.85rem;">
                                            <option value="PENDING" ${o.status eq 'PENDING' ? 'selected' : ''}>PENDING</option>
                                            <option value="CONFIRMED" ${o.status eq 'CONFIRMED' ? 'selected' : ''}>CONFIRMED</option>
                                            <option value="SHIPPED" ${o.status eq 'SHIPPED' ? 'selected' : ''}>SHIPPED</option>
                                            <option value="DELIVERED" ${o.status eq 'DELIVERED' ? 'selected' : ''}>DELIVERED</option>
                                            <option value="CANCELLED" ${o.status eq 'CANCELLED' ? 'selected' : ''}>CANCELLED</option>
                                        </select>
                                        <button type="submit" class="btn btn-outline btn-sm">Update</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:when>
        <c:otherwise>
            <div style="text-align: center; padding: 4rem 1rem; background: var(--surface); border-radius: var(--radius-xl); border: 1px solid var(--border);">
                <h3 style="font-size: 1.5rem; color: var(--text-secondary); margin-bottom: 0.5rem;">No Orders Received Yet</h3>
                <p style="color: var(--text-muted);">Orders placed by buyers containing your products will appear here.</p>
            </div>
        </c:otherwise>
    </c:choose>

</main>

<%@ include file="/WEB-INF/jsp/common/footer.jsp" %>
