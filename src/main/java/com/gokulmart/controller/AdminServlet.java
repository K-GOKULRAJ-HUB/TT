package com.gokulmart.controller;

import com.gokulmart.model.Order;
import com.gokulmart.model.OrderStatus;
import com.gokulmart.model.Product;
import com.gokulmart.model.Role;
import com.gokulmart.model.User;
import com.gokulmart.service.OrderService;
import com.gokulmart.service.ProductService;
import com.gokulmart.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet(urlPatterns = {
    "/admin/dashboard",
    "/admin/users",
    "/admin/products",
    "/admin/product/delete",
    "/admin/orders",
    "/admin/order/update"
})
public class AdminServlet extends HttpServlet {

    private final UserService userService = new UserService();
    private final ProductService productService = new ProductService();
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/admin/users".equals(path)) {
            List<User> users = userService.getAllUsers();
            req.setAttribute("users", users);
            req.getRequestDispatcher("/WEB-INF/jsp/admin/users.jsp").forward(req, resp);
        } else if ("/admin/products".equals(path)) {
            List<Product> products = productService.getAllProducts();
            req.setAttribute("products", products);
            req.getRequestDispatcher("/WEB-INF/jsp/admin/products.jsp").forward(req, resp);
        } else if ("/admin/orders".equals(path)) {
            List<Order> orders = orderService.getAllOrders();
            req.setAttribute("orders", orders);
            req.getRequestDispatcher("/WEB-INF/jsp/admin/orders.jsp").forward(req, resp);
        } else {
            // Dashboard
            int totalUsers = userService.getTotalUsersCount();
            int buyerCount = userService.getUsersByRoleCount(Role.BUYER);
            int sellerCount = userService.getUsersByRoleCount(Role.SELLER);
            int totalProducts = productService.getTotalProductsCount();
            int totalOrders = orderService.getTotalOrdersCount();

            List<User> recentUsers = userService.getAllUsers().stream().limit(5).toList();
            List<Order> recentOrders = orderService.getAllOrders().stream().limit(5).toList();

            req.setAttribute("totalUsers", totalUsers);
            req.setAttribute("buyerCount", buyerCount);
            req.setAttribute("sellerCount", sellerCount);
            req.setAttribute("totalProducts", totalProducts);
            req.setAttribute("totalOrders", totalOrders);
            req.setAttribute("recentUsers", recentUsers);
            req.setAttribute("recentOrders", recentOrders);

            req.getRequestDispatcher("/WEB-INF/jsp/admin/dashboard.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        try {
            if ("/admin/product/delete".equals(path)) {
                Long productId = Long.parseLong(req.getParameter("id"));
                productService.deleteProduct(productId, null); // null sellerId allows admin delete
                String msg = URLEncoder.encode("Product deleted by Admin.", StandardCharsets.UTF_8);
                resp.sendRedirect(req.getContextPath() + "/admin/products?success=" + msg);
            } else if ("/admin/order/update".equals(path)) {
                Long orderId = Long.parseLong(req.getParameter("orderId"));
                OrderStatus newStatus = OrderStatus.valueOf(req.getParameter("status"));
                orderService.updateOrderStatus(orderId, newStatus);
                String msg = URLEncoder.encode("Order #" + orderId + " updated to " + newStatus.name(), StandardCharsets.UTF_8);
                resp.sendRedirect(req.getContextPath() + "/admin/orders?success=" + msg);
            }
        } catch (Exception e) {
            String errorMsg = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard?error=" + errorMsg);
        }
    }
}
