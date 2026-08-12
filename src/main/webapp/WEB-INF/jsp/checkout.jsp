<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="/WEB-INF/jsp/common/header.jsp" %>
<%@ include file="/WEB-INF/jsp/common/navbar.jsp" %>

<main class="main-content container">

    <div class="section-header">
        <h1 class="section-title">Checkout & Place Order</h1>
    </div>

    <c:if test="${not empty param.error}">
        <div class="alert alert-danger">${param.error}</div>
    </c:if>

    <div style="display: grid; grid-template-columns: 2fr 1fr; gap: 2rem; align-items: start;">
        <div>
            <!-- Shipping Info Card -->
            <div style="background: var(--surface); border: 1px solid var(--border); border-radius: var(--radius-xl); padding: 1.75rem; margin-bottom: 2rem;">
                <h3 style="font-size: 1.25rem; font-weight: 700; margin-bottom: 1rem; color: var(--primary-hover);">1. Delivery Information</h3>
                <p><strong>Recipient:</strong> ${sessionScope.user.name}</p>
                <p><strong>Email:</strong> ${sessionScope.user.email}</p>
                <p style="color: var(--text-secondary); margin-top: 0.5rem;"><strong>Shipping Address:</strong> Capstone Campus Delivery Hub, Gokul Mart Express Counter #42, Main Block.</p>
            </div>

            <!-- Mock Payment Options Card -->
            <div style="background: var(--surface); border: 1px solid var(--border); border-radius: var(--radius-xl); padding: 1.75rem; margin-bottom: 2rem;">
                <h3 style="font-size: 1.25rem; font-weight: 700; margin-bottom: 1rem; color: var(--accent-hover);">2. Mock Payment Gateway</h3>
                <div style="background: var(--surface-secondary); padding: 1rem; border-radius: var(--radius-md); border: 1px solid var(--border-bright);">
                    <label style="display: flex; align-items: center; gap: 0.75rem; cursor: pointer; font-weight: 600;">
                        <input type="radio" name="paymentMethod" value="MOCK_PAYMENT" checked style="accent-color: var(--accent);">
                        <span>Instant Capstone Demo Payment (Auto-Approved)</span>
                    </label>
                    <p style="font-size: 0.85rem; color: var(--text-muted); margin-left: 2rem; margin-top: 0.25rem;">
                        No actual credit card required. Order transaction is verified and processed atomically.
                    </p>
                </div>
            </div>

            <!-- Items Review -->
            <div style="background: var(--surface); border: 1px solid var(--border); border-radius: var(--radius-xl); padding: 1.75rem;">
                <h3 style="font-size: 1.25rem; font-weight: 700; margin-bottom: 1rem;">3. Review Order Items</h3>
                <c:forEach var="item" items="${cartItems}">
                    <div style="display: flex; justify-content: space-between; align-items: center; padding: 0.75rem 0; border-bottom: 1px solid var(--border);">
                        <div>
                            <strong>${item.product.name}</strong>
                            <div style="font-size: 0.85rem; color: var(--text-muted);">Qty: ${item.quantity} x &#8377;<fmt:formatNumber value="${item.product.price}" pattern="#,##0.00"/></div>
                        </div>
                        <div style="font-weight: 700;">&#8377;<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/></div>
                    </div>
                </c:forEach>
            </div>
        </div>

        <!-- Place Order Box -->
        <div style="background: var(--surface); border: 1px solid var(--border); border-radius: var(--radius-xl); padding: 1.75rem; position: sticky; top: 90px;">
            <h3 style="font-size: 1.25rem; font-weight: 800; margin-bottom: 1.25rem; border-bottom: 1px solid var(--border); padding-bottom: 0.75rem;">Final Total</h3>

            <div style="display: flex; justify-content: space-between; margin-bottom: 1.5rem; font-size: 1.5rem; font-weight: 800;">
                <span>Total</span>
                <span style="color: var(--accent);">&#8377;<fmt:formatNumber value="${cartTotal}" pattern="#,##0.00"/></span>
            </div>

            <form action="${pageContext.request.contextPath}/checkout/place-order" method="POST">
                <button type="submit" class="btn btn-accent btn-lg" style="width: 100%;">
                    Confirm & Place Order &rarr;
                </button>
            </form>
        </div>
    </div>

</main>

<%@ include file="/WEB-INF/jsp/common/footer.jsp" %>
