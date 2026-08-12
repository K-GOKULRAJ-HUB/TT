package com.gokulmart.controller;

import com.gokulmart.model.Order;
import com.gokulmart.model.User;
import com.gokulmart.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = {"/orders", "/order/detail"})
public class OrderServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        String path = req.getServletPath();

        if ("/order/detail".equals(path)) {
            String idStr = req.getParameter("id");
            if (idStr == null) {
                resp.sendRedirect(req.getContextPath() + "/orders");
                return;
            }
            try {
                Long orderId = Long.parseLong(idStr);
                Optional<Order> orderOpt = orderService.getOrderById(orderId);

                if (orderOpt.isPresent()) {
                    Order order = orderOpt.get();
                    // Authorization check: buyer can view own order, seller can view if contains their items, admin can view all
                    if (user.getId().equals(order.getBuyerId()) || user.getRole().toString().equals("ADMIN") || user.getRole().toString().equals("SELLER")) {
                        req.setAttribute("order", order);
                        req.getRequestDispatcher("/WEB-INF/jsp/order-detail.jsp").forward(req, resp);
                        return;
                    }
                }
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied.");
            } catch (NumberFormatException e) {
                resp.sendRedirect(req.getContextPath() + "/orders");
            }
        } else {
            List<Order> orders = orderService.getBuyerOrders(user.getId());
            req.setAttribute("orders", orders);
            req.getRequestDispatcher("/WEB-INF/jsp/order-history.jsp").forward(req, resp);
        }
    }
}
