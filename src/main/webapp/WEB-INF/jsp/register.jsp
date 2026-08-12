<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="/WEB-INF/jsp/common/header.jsp" %>
<%@ include file="/WEB-INF/jsp/common/navbar.jsp" %>

<main class="main-content container">

    <div class="form-card">
        <div style="text-align: center; margin-bottom: 2rem;">
            <h2 style="font-size: 1.8rem; font-weight: 800; margin-bottom: 0.5rem;">Create an Account</h2>
            <p style="color: var(--text-secondary); font-size: 0.95rem;">Join Gokul Mart as a Buyer or Seller</p>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <form id="registerForm" action="${pageContext.request.contextPath}/register" method="POST">
            <div class="form-group">
                <label class="form-label" for="name">Full Name / Business Name</label>
                <input type="text" id="name" name="name" class="form-control" value="${name}" placeholder="John Doe" required>
            </div>

            <div class="form-group">
                <label class="form-label" for="email">Email Address</label>
                <input type="email" id="email" name="email" class="form-control" value="${email}" placeholder="you@example.com" required>
            </div>

            <div class="form-group">
                <label class="form-label" for="role">Account Type</label>
                <select id="role" name="role" class="form-control">
                    <option value="BUYER">Buyer (Shop & Purchase)</option>
                    <option value="SELLER">Seller (List & Sell Products)</option>
                </select>
            </div>

            <div class="form-group">
                <label class="form-label" for="password">Password</label>
                <input type="password" id="password" name="password" class="form-control" placeholder="At least 6 characters" required minlength="6">
            </div>

            <div class="form-group">
                <label class="form-label" for="confirmPassword">Confirm Password</label>
                <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" placeholder="Re-enter password" required minlength="6">
            </div>

            <button type="submit" class="btn btn-accent" style="width: 100%; margin-top: 1rem;">Create Account &rarr;</button>
        </form>

        <div style="text-align: center; margin-top: 1.5rem; color: var(--text-secondary); font-size: 0.9rem;">
            Already have an account? <a href="${pageContext.request.contextPath}/login" style="color: var(--primary-hover); font-weight: 600;">Sign in here</a>
        </div>
    </div>

</main>

<%@ include file="/WEB-INF/jsp/common/footer.jsp" %>
