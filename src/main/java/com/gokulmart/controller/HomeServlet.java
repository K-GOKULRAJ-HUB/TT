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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Product> allProducts = productService.getAllProducts();
        
        // Limit featured products on home page to top 8
        List<Product> featuredProducts = allProducts.stream().limit(8).toList();

        req.setAttribute("featuredProducts", featuredProducts);
        req.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(req, resp);
    }
}
