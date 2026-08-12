<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="/WEB-INF/jsp/common/header.jsp" %>
<%@ include file="/WEB-INF/jsp/common/navbar.jsp" %>

<main class="main-content container">

    <div class="section-header">
        <h1 class="section-title">Shopping Cart</h1>
    </div>

    <c:if test="${not empty param.success}">
        <div class="alert alert-success">${param.success}</div>
    </c:if>
    <c:if test="${not empty param.error}">
        <div class="alert alert-danger">${param.error}</div>
    </c:if>

    <c:choose>
        <c:when test="${not empty cartItems}">
            <div style="display: grid; grid-template-columns: 2.5fr 1fr; gap: 2rem; align-items: start;">
                <div class="table-responsive">
                    <table class="custom-table">
                        <thead>
                            <tr>
                                <th>Product</th>
                                <th>Unit Price</th>
                                <th>Quantity</th>
                                <th>Subtotal</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${cartItems}">
                                <tr>
                                    <td>
                                        <div style="display: flex; gap: 1rem; align-items: center;">
                                            <img src="${item.product.imageUrl}" alt="${item.product.name}" style="width: 60px; height: 60px; object-fit: cover; border-radius: var(--radius-md);" onerror="this.src='https://images.unsplash.com/photo-1560343090-f0409e92791a?w=600&auto=format&fit=crop'">
                                            <div>
                                                <a href="${pageContext.request.contextPath}/product/detail?id=${item.product.id}" style="font-weight: 700;">${item.product.name}</a>
                                                <div style="font-size: 0.8rem; color: var(--text-muted);">Seller: ${item.product.sellerName}</div>
                                            </div>
                                        </div>
                                    </td>
                                    <td>&#8377;<fmt:formatNumber value="${item.product.price}" pattern="#,##0.00"/></td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/cart/update" method="POST" style="display: flex; gap: 0.5rem; align-items: center;">
                                            <input type="hidden" name="cartItemId" value="${item.id}">
                                            <input type="number" name="quantity" value="${item.quantity}" min="1" max="${item.product.stock}" class="form-control" style="width: 70px; text-align: center; padding: 0.3rem;" onchange="this.form.submit()">
                                        </form>
                                    </td>
                                    <td style="font-weight: 700;">&#8377;<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/></td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/cart/remove" method="POST">
                                            <input type="hidden" name="cartItemId" value="${item.id}">
                                            <button type="submit" class="btn btn-danger btn-sm">Remove</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <!-- Summary Box -->
                <div style="background: var(--surface); border: 1px solid var(--border); border-radius: var(--radius-xl); padding: 1.75rem;">
                    <h3 style="font-size: 1.25rem; font-weight: 800; margin-bottom: 1.25rem; border-bottom: 1px solid var(--border); padding-bottom: 0.75rem;">Order Summary</h3>
                    
                    <div style="display: flex; justify-content: space-between; margin-bottom: 0.75rem; color: var(--text-secondary);">
                        <span>Items Subtotal</span>
                        <span>&#8377;<fmt:formatNumber value="${cartTotal}" pattern="#,##0.00"/></span>
                    </div>
                    <div style="display: flex; justify-content: space-between; margin-bottom: 1.25rem; color: var(--text-secondary);">
                        <span>Shipping Fee</span>
                        <span style="color: var(--success); font-weight: 600;">FREE</span>
                    </div>

                    <div style="display: flex; justify-content: space-between; margin-bottom: 1.75rem; border-top: 1px solid var(--border); padding-top: 1rem; font-size: 1.3rem; font-weight: 800;">
                        <span>Total Amount</span>
                        <span style="color: var(--primary-hover);">&#8377;<fmt:formatNumber value="${cartTotal}" pattern="#,##0.00"/></span>
                    </div>

                    <a href="${pageContext.request.contextPath}/checkout" class="btn btn-accent btn-lg" style="width: 100%;">
                        Proceed to Checkout &rarr;
                    </a>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div style="text-align: center; padding: 4rem 1rem; background: var(--surface); border-radius: var(--radius-xl); border: 1px solid var(--border);">
                <h3 style="font-size: 1.5rem; color: var(--text-secondary); margin-bottom: 0.5rem;">Your Shopping Cart is Empty</h3>
                <p style="color: var(--text-muted); margin-bottom: 1.5rem;">Explore Gokul Mart listings and add items to your cart.</p>
                <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">Browse Marketplace</a>
            </div>
        </c:otherwise>
    </c:choose>

</main>

<%@ include file="/WEB-INF/jsp/common/footer.jsp" %>
