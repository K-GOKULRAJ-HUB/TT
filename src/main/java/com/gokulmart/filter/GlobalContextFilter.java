package com.gokulmart.filter;

import com.gokulmart.model.Role;
import com.gokulmart.model.User;
import com.gokulmart.service.WishlistService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class GlobalContextFilter implements Filter {

    private final WishlistService wishlistService = new WishlistService();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        String path = req.getRequestURI().substring(req.getContextPath().length());

        // Skip static resources
        if (path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/images/")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user != null && user.getRole() == Role.BUYER) {
            int wishlistCount = wishlistService.getWishlistedProductIdsByUser(user.getId()).size();
            req.setAttribute("wishlistCount", wishlistCount);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
