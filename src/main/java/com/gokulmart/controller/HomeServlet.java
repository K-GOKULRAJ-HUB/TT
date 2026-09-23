package com.gokulmart.controller;

import com.gokulmart.model.Product;
import com.gokulmart.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"", "/home"})
public class HomeServlet extends HttpServlet {

    private final ProductService productService = new ProductService();
    private final com.gokulmart.service.WishlistService wishlistService = new com.gokulmart.service.WishlistService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Product> allProducts = productService.getAllProducts();
        
        // Limit featured products on home page to top 8
        List<Product> featuredProducts = allProducts.stream().limit(8).toList();

        javax.servlet.http.HttpSession session = req.getSession(false);
        com.gokulmart.model.User user = (session != null) ? (com.gokulmart.model.User) session.getAttribute("user") : null;
        if (user != null && user.getRole() == com.gokulmart.model.Role.BUYER) {
            java.util.Set<Long> wishlistedIds = wishlistService.getWishlistedProductIdsByUser(user.getId());
            for (Product p : featuredProducts) {
                if (wishlistedIds.contains(p.getId())) {
                    p.setWishlisted(true);
                }
            }
        }

        req.setAttribute("featuredProducts", featuredProducts);
        req.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(req, resp);
    }
}
