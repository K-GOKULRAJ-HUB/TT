package com.gokulmart.controller;

import com.gokulmart.model.Order;
import com.gokulmart.model.OrderStatus;
import com.gokulmart.model.Product;
import com.gokulmart.model.User;
import com.gokulmart.service.OrderService;
import com.gokulmart.service.ProductService;

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
import java.util.Optional;

@WebServlet(urlPatterns = {
    "/seller/dashboard",
    "/seller/products",
    "/seller/product/create",
    "/seller/product/edit",
    "/seller/product/delete",
    "/seller/orders",
    "/seller/order/update"
})
public class SellerServlet extends HttpServlet {

    private final ProductService productService = new ProductService();
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User seller = (User) session.getAttribute("user");
        String path = req.getServletPath();

        if ("/seller/product/create".equals(path)) {
            req.getRequestDispatcher("/WEB-INF/jsp/seller/product-form.jsp").forward(req, resp);
        } else if ("/seller/product/edit".equals(path)) {
            String idStr = req.getParameter("id");
            if (idStr != null) {
                Long pId = Long.parseLong(idStr);
                Optional<Product> pOpt = productService.getProductById(pId);
                if (pOpt.isPresent() && pOpt.get().getSellerId().equals(seller.getId())) {
                    req.setAttribute("product", pOpt.get());
                    req.getRequestDispatcher("/WEB-INF/jsp/seller/product-form.jsp").forward(req, resp);
                    return;
                }
            }
            resp.sendRedirect(req.getContextPath() + "/seller/dashboard");
        } else if ("/seller/orders".equals(path)) {
            List<Order> orders = orderService.getSellerOrders(seller.getId());
            req.setAttribute("orders", orders);
            req.getRequestDispatcher("/WEB-INF/jsp/seller/orders.jsp").forward(req, resp);
        } else {
            // Dashboard view
            List<Product> products = productService.getProductsBySeller(seller.getId());
            List<Order> orders = orderService.getSellerOrders(seller.getId());
            int totalStock = products.stream().mapToInt(Product::getStock).sum();

            req.setAttribute("products", products);
            req.setAttribute("orders", orders);
            req.setAttribute("totalProductsCount", products.size());
            req.setAttribute("totalOrdersCount", orders.size());
            req.setAttribute("totalStock", totalStock);

            req.getRequestDispatcher("/WEB-INF/jsp/seller/dashboard.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User seller = (User) session.getAttribute("user");
        String path = req.getServletPath();

        try {
            if ("/seller/product/create".equals(path)) {
                String name = req.getParameter("name");
                String description = req.getParameter("description");
                BigDecimal price = new BigDecimal(req.getParameter("price"));
                int stock = Integer.parseInt(req.getParameter("stock"));
                String category = req.getParameter("category");
                String imageUrl = req.getParameter("imageUrl");

                productService.createProduct(seller.getId(), name, description, price, stock, category, imageUrl);
                String msg = URLEncoder.encode("Product created successfully!", StandardCharsets.UTF_8);
                resp.sendRedirect(req.getContextPath() + "/seller/dashboard?success=" + msg);

            } else if ("/seller/product/edit".equals(path)) {
                Long productId = Long.parseLong(req.getParameter("id"));
                String name = req.getParameter("name");
                String description = req.getParameter("description");
                BigDecimal price = new BigDecimal(req.getParameter("price"));
                int stock = Integer.parseInt(req.getParameter("stock"));
                String category = req.getParameter("category");
                String imageUrl = req.getParameter("imageUrl");

                productService.updateProduct(productId, seller.getId(), name, description, price, stock, category, imageUrl);
                String msg = URLEncoder.encode("Product updated successfully!", StandardCharsets.UTF_8);
                resp.sendRedirect(req.getContextPath() + "/seller/dashboard?success=" + msg);

            } else if ("/seller/product/delete".equals(path)) {
                Long productId = Long.parseLong(req.getParameter("id"));
                productService.deleteProduct(productId, seller.getId());
                String msg = URLEncoder.encode("Product deleted successfully!", StandardCharsets.UTF_8);
                resp.sendRedirect(req.getContextPath() + "/seller/dashboard?success=" + msg);

            } else if ("/seller/order/update".equals(path)) {
                Long orderId = Long.parseLong(req.getParameter("orderId"));
                OrderStatus newStatus = OrderStatus.valueOf(req.getParameter("status"));
                orderService.updateOrderStatus(orderId, newStatus);
                String msg = URLEncoder.encode("Order status updated to " + newStatus.name(), StandardCharsets.UTF_8);
                resp.sendRedirect(req.getContextPath() + "/seller/orders?success=" + msg);
            }
        } catch (Exception e) {
            String errorMsg = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            resp.sendRedirect(req.getContextPath() + "/seller/dashboard?error=" + errorMsg);
        }
    }
}
