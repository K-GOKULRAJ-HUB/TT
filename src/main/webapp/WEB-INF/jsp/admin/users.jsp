<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="/WEB-INF/jsp/common/header.jsp" %>
<%@ include file="/WEB-INF/jsp/common/navbar.jsp" %>

<main class="main-content container">

    <div class="section-header">
        <div>
            <h1 class="section-title">User Accounts Management</h1>
            <p style="color: var(--text-secondary);">Overview of all registered Buyers, Sellers, and Administrators</p>
        </div>
    </div>

    <div class="table-responsive">
        <table class="custom-table">
            <thead>
                <tr>
                    <th>User ID</th>
                    <th>Full Name</th>
                    <th>Email Address</th>
                    <th>Assigned Role</th>
                    <th>Registration Date</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="u" items="${users}">
                    <tr>
                        <td style="font-weight: 700;">#${u.id}</td>
                        <td><strong>${u.name}</strong></td>
                        <td>${u.email}</td>
                        <td><span class="role-tag ${u.role.name().toLowerCase()}">${u.role}</span></td>
                        <td>${u.createdAt}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

</main>

<%@ include file="/WEB-INF/jsp/common/footer.jsp" %>
