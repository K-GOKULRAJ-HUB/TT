<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="/WEB-INF/jsp/common/header.jsp" %>
<%@ include file="/WEB-INF/jsp/common/navbar.jsp" %>

<main class="main-content container">

    <c:if test="${not empty param.success}">
        <div class="alert alert-success">${param.success}</div>
    </c:if>

    <div style="background: var(--surface); border: 1px solid var(--border); border-radius: var(--radius-xl); padding: 2.5rem; max-width: 900px; margin: 0 auto;">
        <div style="display: flex; justify-content: space-between; align-items: flex-start; border-bottom: 1px solid var(--border); padding-bottom: 1.5rem; margin-bottom: 2rem;">
            <div>
                <span class="brand-logo" style="font-size: 1.8rem;">
                    <span class="brand-gokul">Gokul</span><span class="brand-mart">Mart</span>
                </span>
                <p style="color: var(--text-secondary); margin-top: 0.25rem;">Multi-Seller E-Commerce Marketplace Invoice</p>
            </div>
            <div style="text-align: right;">
                <h2 style="font-size: 1.5rem; font-weight: 800;">Order #${order.id}</h2>
                <div style="margin-top: 0.5rem;"><span class="badge badge-${order.status.name().toLowerCase()}">${order.status}</span></div>
                <small style="color: var(--text-muted); display: block; margin-top: 0.5rem;">Date: ${order.createdAt}</small>
            </div>
        </div>

        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 2rem; margin-bottom: 2rem;">
            <div>
                <h4 style="color: var(--text-secondary); font-size: 0.85rem; text-transform: uppercase; margin-bottom: 0.5rem;">Billed To (Buyer)</h4>
                <p><strong>${order.buyerName}</strong></p>
                <p style="color: var(--text-secondary);">${order.buyerEmail}</p>
            </div>
            <div>
                <h4 style="color: var(--text-secondary); font-size: 0.85rem; text-transform: uppercase; margin-bottom: 0.5rem;">Payment Method</h4>
                <p><strong>Capstone Demo Payment</strong> (Mock Settlement)</p>
            </div>
        </div>

        <h3 style="font-size: 1.2rem; font-weight: 700; margin-bottom: 1rem;">Order Items</h3>
        <div class="table-responsive" style="margin-bottom: 2rem;">
            <table class="custom-table">
                <thead>
                    <tr>
                        <th>Product</th>
                        <th>Seller</th>
                        <th>Unit Price</th>
                        <th>Quantity</th>
                        <th>Subtotal</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${order.items}">
                        <tr>
                            <td>
                                <a href="${pageContext.request.contextPath}/product/detail?id=${item.product.id}" style="font-weight: 700;">${item.product.name}</a>
                            </td>
                            <td>${item.product.sellerName}</td>
                            <td>&#8377;<fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.00"/></td>
                            <td>${item.quantity}</td>
                            <td style="font-weight: 700;">&#8377;<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <div style="display: flex; justify-content: flex-end; border-top: 1px solid var(--border); padding-top: 1.5rem;">
            <div style="text-align: right;">
                <div style="font-size: 1rem; color: var(--text-secondary); margin-bottom: 0.5rem;">Grand Total Paid</div>
                <div style="font-size: 2rem; font-weight: 800; color: var(--primary-hover);">&#8377;<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></div>
            </div>
        </div>
    </div>

</main>

<%@ include file="/WEB-INF/jsp/common/footer.jsp" %>
