package com.gokulmart.filter;

import com.gokulmart.model.Role;
import com.gokulmart.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        String path = uri.substring(contextPath.length());

        User user = (session != null) ? (User) session.getAttribute("user") : null;

        // Public static assets & auth endpoints
        if (path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/images/") ||
            path.equals("/login") || path.equals("/register") || path.equals("/logout") ||
            path.equals("/home") || path.equals("/") || path.equals("/products") || path.equals("/product/detail")) {
            chain.doFilter(request, response);
            return;
        }

        // Unauthenticated user attempting protected route
        if (user == null) {
            String redirectMsg = URLEncoder.encode("Please login to continue.", StandardCharsets.UTF_8);
            res.sendRedirect(contextPath + "/login?error=" + redirectMsg);
            return;
        }

        // Role Protection Checks
        if (path.startsWith("/seller/")) {
            if (user.getRole() != Role.SELLER) {
                String errorMsg = URLEncoder.encode("Access denied. Seller privileges required.", StandardCharsets.UTF_8);
                res.sendRedirect(contextPath + "/home?error=" + errorMsg);
                return;
            }
        } else if (path.startsWith("/admin/")) {
            if (user.getRole() != Role.ADMIN) {
                String errorMsg = URLEncoder.encode("Access denied. Administrator privileges required.", StandardCharsets.UTF_8);
                res.sendRedirect(contextPath + "/home?error=" + errorMsg);
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
