package com.gokulmart.controller;

import com.gokulmart.model.CartItem;
import com.gokulmart.model.Order;
import com.gokulmart.model.User;
import com.gokulmart.service.CartService;
import com.gokulmart.service.OrderService;

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

@WebServlet(urlPatterns = {"/checkout", "/checkout/place-order"})
public class CheckoutServlet extends HttpServlet {

    private final CartService cartService = new CartService();
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        List<CartItem> cartItems = cartService.getCartItems(user.getId());
        if (cartItems.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        BigDecimal total = cartService.calculateCartTotal(cartItems);

        req.setAttribute("cartItems", cartItems);
        req.setAttribute("cartTotal", total);

        req.getRequestDispatcher("/WEB-INF/jsp/checkout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        try {
            Order order = orderService.placeOrder(user.getId());
            String successMsg = URLEncoder.encode("Order #" + order.getId() + " placed successfully! Thank you for shopping at Gokul Mart.", StandardCharsets.UTF_8);
            resp.sendRedirect(req.getContextPath() + "/order/detail?id=" + order.getId() + "&success=" + successMsg);
        } catch (Exception e) {
            String errorMsg = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            resp.sendRedirect(req.getContextPath() + "/checkout?error=" + errorMsg);
        }
    }
}
