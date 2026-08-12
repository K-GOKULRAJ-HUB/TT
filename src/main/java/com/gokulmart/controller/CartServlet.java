package com.gokulmart.controller;

import com.gokulmart.model.CartItem;
import com.gokulmart.model.User;
import com.gokulmart.service.CartService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet(urlPatterns = {"/cart", "/cart/add", "/cart/update", "/cart/remove"})
public class CartServlet extends HttpServlet {

    private final CartService cartService = new CartService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        List<CartItem> cartItems = cartService.getCartItems(user.getId());
        BigDecimal total = cartService.calculateCartTotal(cartItems);

        req.setAttribute("cartItems", cartItems);
        req.setAttribute("cartTotal", total);

        req.getRequestDispatcher("/WEB-INF/jsp/cart.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        String path = req.getServletPath();

        try {
            if ("/cart/add".equals(path)) {
                Long productId = Long.parseLong(req.getParameter("productId"));
                int quantity = 1;
                if (req.getParameter("quantity") != null) {
                    quantity = Integer.parseInt(req.getParameter("quantity"));
                }
                cartService.addToCart(user.getId(), productId, quantity);
                String msg = URLEncoder.encode("Product added to cart successfully!", StandardCharsets.UTF_8);
                resp.sendRedirect(req.getContextPath() + "/cart?success=" + msg);

            } else if ("/cart/update".equals(path)) {
                Long cartItemId = Long.parseLong(req.getParameter("cartItemId"));
                int quantity = Integer.parseInt(req.getParameter("quantity"));
                cartService.updateCartQuantity(cartItemId, user.getId(), quantity);
                resp.sendRedirect(req.getContextPath() + "/cart");

            } else if ("/cart/remove".equals(path)) {
                Long cartItemId = Long.parseLong(req.getParameter("cartItemId"));
                cartService.removeFromCart(cartItemId, user.getId());
                resp.sendRedirect(req.getContextPath() + "/cart");
            }
        } catch (Exception e) {
            String errorMsg = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            resp.sendRedirect(req.getContextPath() + "/cart?error=" + errorMsg);
        }
    }
}
