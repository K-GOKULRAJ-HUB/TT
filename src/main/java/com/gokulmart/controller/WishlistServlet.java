package com.gokulmart.controller;

import com.gokulmart.model.Product;
import com.gokulmart.model.Role;
import com.gokulmart.model.User;
import com.gokulmart.service.WishlistService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/wishlist", "/wishlist/add", "/wishlist/remove"})
public class WishlistServlet extends HttpServlet {

    private final WishlistService wishlistService = new WishlistService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null || user.getRole() != Role.BUYER) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if ("/wishlist".equals(path)) {
            List<Product> wishlist = wishlistService.getWishlistByUser(user.getId());
            req.setAttribute("wishlist", wishlist);
            req.getRequestDispatcher("/WEB-INF/jsp/wishlist.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null || user.getRole() != Role.BUYER) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Buyer login required.");
            return;
        }

        long productId;
        try {
            productId = Long.parseLong(req.getParameter("productId"));
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product ID.");
            return;
        }

        String msg = "";
        if ("/wishlist/add".equals(path)) {
            wishlistService.addProductToWishlist(user.getId(), productId);
            msg = "Product added to Wishlist";
        } else if ("/wishlist/remove".equals(path)) {
            wishlistService.removeProductFromWishlist(user.getId(), productId);
            msg = "Product removed from Wishlist";
        }

        String encodedMsg = java.net.URLEncoder.encode(msg, java.nio.charset.StandardCharsets.UTF_8);
        String referer = req.getHeader("Referer");
        if (referer != null) {
            if (referer.contains("?")) {
                referer = referer.replaceAll("(&|\\?)success=[^&]*", "").replaceAll("(&|\\?)error=[^&]*", "");
                referer += (referer.contains("?") ? "&" : "?") + "success=" + encodedMsg;
            } else {
                referer += "?success=" + encodedMsg;
            }
            resp.sendRedirect(referer);
        } else {
            resp.sendRedirect(req.getContextPath() + "/wishlist?success=" + encodedMsg);
        }
    }
}
