<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="/WEB-INF/jsp/common/header.jsp" %>
<%@ include file="/WEB-INF/jsp/common/navbar.jsp" %>

<main class="main-content container">

    <div style="text-align: center; padding: 5rem 1rem; background: var(--surface); border-radius: var(--radius-xl); border: 1px solid var(--border); max-width: 600px; margin: 3rem auto;">
        <div style="font-size: 5rem; font-weight: 800; color: var(--danger); margin-bottom: 1rem;">500</div>
        <h1 style="font-size: 1.8rem; font-weight: 800; margin-bottom: 0.75rem;">Internal Marketplace Error</h1>
        <p style="color: var(--text-secondary); margin-bottom: 2rem;">An unexpected server error occurred. Please try again or return home.</p>
        <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">Return to Gokul Mart Home</a>
    </div>

</main>

<%@ include file="/WEB-INF/jsp/common/footer.jsp" %>
