package com.gokulmart.controller;

import com.gokulmart.model.User;
import com.gokulmart.service.ReviewService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet(urlPatterns = {"/review/add"})
public class ReviewServlet extends HttpServlet {

    private final ReviewService reviewService = new ReviewService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        String productIdStr = req.getParameter("productId");
        String ratingStr = req.getParameter("rating");
        String comment = req.getParameter("comment");

        if (productIdStr == null || ratingStr == null) {
            resp.sendRedirect(req.getContextPath() + "/products");
            return;
        }

        try {
            Long productId = Long.parseLong(productIdStr);
            int rating = Integer.parseInt(ratingStr);

            reviewService.addReview(user.getId(), productId, rating, comment);
            String successMsg = URLEncoder.encode("Review submitted successfully! Thank you.", StandardCharsets.UTF_8);
            resp.sendRedirect(req.getContextPath() + "/product/detail?id=" + productId + "&success=" + successMsg);
        } catch (Exception e) {
            String errorMsg = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            resp.sendRedirect(req.getContextPath() + "/product/detail?id=" + productIdStr + "&error=" + errorMsg);
        }
    }
}
