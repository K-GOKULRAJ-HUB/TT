<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<nav class="navbar">
    <div class="container navbar-inner">
        <a href="${pageContext.request.contextPath}/home" class="brand-logo">
            <span class="brand-gokul">Gokul</span><span class="brand-mart">Mart</span>
        </a>

    <ul class="nav-links">
        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                <c:if test="${sessionScope.user.role eq 'BUYER'}">
                    <li><a href="${pageContext.request.contextPath}/home" class="nav-link">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/products" class="nav-link">Products</a></li>
                    <li><a href="${pageContext.request.contextPath}/wishlist" class="nav-link">Wishlist</a></li>
                    <li><a href="${pageContext.request.contextPath}/cart" class="nav-link">Cart</a></li>
                    <li><a href="${pageContext.request.contextPath}/orders" class="nav-link">Orders</a></li>
                </c:if>
                <c:if test="${sessionScope.user.role eq 'SELLER'}">
                    <li><a href="${pageContext.request.contextPath}/seller/dashboard" class="nav-link">Seller Dashboard</a></li>
                    <li><a href="${pageContext.request.contextPath}/seller/dashboard" class="nav-link">My Products</a></li>
                    <li><a href="${pageContext.request.contextPath}/seller/product/create" class="nav-link">Add Product</a></li>
                    <li><a href="${pageContext.request.contextPath}/seller/orders" class="nav-link">Orders</a></li>
                </c:if>
                <c:if test="${sessionScope.user.role eq 'ADMIN'}">
                    <li><a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link">Admin Dashboard</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/users" class="nav-link">Users</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/products" class="nav-link">Products</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link">Orders</a></li>
                </c:if>

                    <li>
                        <div class="user-badge">
                            <span>${sessionScope.user.name}</span>
                            <span class="role-tag ${sessionScope.user.role.name().toLowerCase()}">${sessionScope.user.role}</span>
                        </div>
                    </li>
                    <li><a href="${pageContext.request.contextPath}/logout" class="btn btn-outline btn-sm">Logout</a></li>
                </c:when>
                <c:otherwise>
                    <li><a href="${pageContext.request.contextPath}/home" class="nav-link">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/products" class="nav-link">Products</a></li>
                    <li><a href="${pageContext.request.contextPath}/login" class="nav-link">Login</a></li>
                    <li><a href="${pageContext.request.contextPath}/register" class="btn btn-accent btn-sm">Register</a></li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>
</nav>
