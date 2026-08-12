<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="/WEB-INF/jsp/common/header.jsp" %>
<%@ include file="/WEB-INF/jsp/common/navbar.jsp" %>

<main class="main-content container">

    <div class="section-header">
        <h1 class="section-title">My Order History</h1>
    </div>

    <c:choose>
        <c:when test="${not empty orders}">
            <div class="table-responsive">
                <table class="custom-table">
                    <thead>
                        <tr>
                            <th>Order ID</th>
                            <th>Date</th>
                            <th>Items</th>
                            <th>Total Amount</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="o" items="${orders}">
                            <tr>
                                <td style="font-weight: 700;">#${o.id}</td>
                                <td>${o.createdAt}</td>
                                <td>${o.items.size()} item(s)</td>
                                <td style="font-weight: 700; color: var(--primary-hover);">&#8377;<fmt:formatNumber value="${o.totalAmount}" pattern="#,##0.00"/></td>
                                <td><span class="badge badge-${o.status.name().toLowerCase()}">${o.status}</span></td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/order/detail?id=${o.id}" class="btn btn-outline btn-sm">View Invoice</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:when>
        <c:otherwise>
            <div style="text-align: center; padding: 4rem 1rem; background: var(--surface); border-radius: var(--radius-xl); border: 1px solid var(--border);">
                <h3 style="font-size: 1.5rem; color: var(--text-secondary); margin-bottom: 0.5rem;">No Orders Placed Yet</h3>
                <p style="color: var(--text-muted); margin-bottom: 1.5rem;">You haven't placed any orders on Gokul Mart yet.</p>
                <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">Start Shopping</a>
            </div>
        </c:otherwise>
    </c:choose>

</main>

<%@ include file="/WEB-INF/jsp/common/footer.jsp" %>
