package com.gokulmart.controller;

import com.gokulmart.model.Product;
import com.gokulmart.model.Review;
import com.gokulmart.model.User;
import com.gokulmart.service.ProductService;
import com.gokulmart.service.ReviewService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = {"/products", "/product/detail"})
public class ProductServlet extends HttpServlet {

    private final ProductService productService = new ProductService();
    private final ReviewService reviewService = new ReviewService();
    private final com.gokulmart.service.WishlistService wishlistService = new com.gokulmart.service.WishlistService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/product/detail".equals(path)) {
            handleProductDetail(req, resp);
        } else {
            handleProductList(req, resp);
        }
    }

    private void handleProductList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String query = req.getParameter("query");
        String category = req.getParameter("category");

        List<Product> products = productService.searchProducts(query, category);

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user != null && user.getRole() == com.gokulmart.model.Role.BUYER) {
            java.util.Set<Long> wishlistedIds = wishlistService.getWishlistedProductIdsByUser(user.getId());
            for (Product p : products) {
                if (wishlistedIds.contains(p.getId())) {
                    p.setWishlisted(true);
                }
            }
        }

        req.setAttribute("products", products);
        req.setAttribute("query", query != null ? query : "");
        req.setAttribute("selectedCategory", category != null ? category : "ALL");

        req.getRequestDispatcher("/WEB-INF/jsp/products.jsp").forward(req, resp);
    }

    private void handleProductDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/products");
            return;
        }

        try {
            Long productId = Long.parseLong(idStr);
            Optional<Product> productOpt = productService.getProductById(productId);

            if (productOpt.isPresent()) {
                Product product = productOpt.get();
                List<Review> reviews = reviewService.getProductReviews(productId);
                double avgRating = reviewService.getAverageRating(productId);

                HttpSession session = req.getSession(false);
                User user = (session != null) ? (User) session.getAttribute("user") : null;

                boolean canReview = false;
                if (user != null) {
                    if (user.getRole() == com.gokulmart.model.Role.BUYER) {
                        java.util.Set<Long> wishlistedIds = wishlistService.getWishlistedProductIdsByUser(user.getId());
                        if (wishlistedIds.contains(productId)) {
                            product.setWishlisted(true);
                        }
                    }
                    canReview = reviewService.canUserReview(user.getId(), productId);
                }

                req.setAttribute("product", product);
                req.setAttribute("reviews", reviews);
                req.setAttribute("avgRating", avgRating);
                req.setAttribute("canReview", canReview);

                req.getRequestDispatcher("/WEB-INF/jsp/product-detail.jsp").forward(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found.");
            }
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/products");
        }
    }
}
