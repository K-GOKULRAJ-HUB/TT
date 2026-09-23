<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="/WEB-INF/jsp/common/header.jsp" %>
<%@ include file="/WEB-INF/jsp/common/navbar.jsp" %>

<main class="main-content container">

    <div class="form-card">
        <div style="text-align: center; margin-bottom: 2rem;">
            <h2 style="font-size: 1.8rem; font-weight: 800; margin-bottom: 0.5rem;">Welcome Back</h2>
            <p style="color: var(--text-secondary); font-size: 0.95rem;">Log in to your Gokul Mart account</p>
        </div>

        <c:if test="${not empty param.success}">
            <div class="alert alert-success">${param.success}</div>
        </c:if>
        <c:if test="${not empty param.error}">
            <div class="alert alert-danger">${param.error}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="POST">
            <div class="form-group" style="text-align: center; margin-bottom: 1.5rem;">
                <label class="form-label" style="display: block; margin-bottom: 0.5rem;">Account Type</label>
                <div style="display: flex; justify-content: center; gap: 1.5rem;">
                    <label>
                        <input type="radio" name="loginType" value="BUYER" checked> Buyer
                    </label>
                    <label>
                        <input type="radio" name="loginType" value="SELLER"> Seller
                    </label>
                    <label>
                        <input type="radio" name="loginType" value="ADMIN"> Admin
                    </label>
                </div>
            </div>

            <div class="form-group">
                <label class="form-label" for="email">Email Address</label>
                <input type="email" id="email" name="email" class="form-control" value="${email}" placeholder="you@example.com" required>
            </div>

            <div class="form-group">
                <label class="form-label" for="password">Password</label>
                <input type="password" id="password" name="password" class="form-control" placeholder="••••••••" required>
            </div>

            <button type="submit" class="btn btn-accent" style="width: 100%; margin-top: 1rem;">Sign In &rarr;</button>
        </form>

        <div style="text-align: center; margin-top: 1.5rem; color: var(--text-secondary); font-size: 0.9rem;">
            Don't have an account? <a href="${pageContext.request.contextPath}/register" style="color: var(--primary-hover); font-weight: 600;">Register here</a>
        </div>
    </div>

</main>

<%@ include file="/WEB-INF/jsp/common/footer.jsp" %>
