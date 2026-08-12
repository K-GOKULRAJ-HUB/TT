package com.gokulmart.service;

import com.gokulmart.dao.ProductDAO;
import com.gokulmart.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ProductService {

    private final ProductDAO productDAO = new ProductDAO();

    public Optional<Product> getProductById(Long id) {
        return productDAO.findById(id);
    }

    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    public List<Product> searchProducts(String query, String category) {
        return productDAO.search(query, category);
    }

    public List<Product> getProductsBySeller(Long sellerId) {
        return productDAO.findBySellerId(sellerId);
    }

    public Product createProduct(Long sellerId, String name, String description, BigDecimal price, Integer stock, String category, String imageUrl) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name is required.");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Product price must be greater than zero.");
        }
        if (stock == null || stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category is required.");
        }

        Product p = new Product();
        p.setSellerId(sellerId);
        p.setName(name.trim());
        p.setDescription(description != null ? description.trim() : "");
        p.setPrice(price);
        p.setStock(stock);
        p.setCategory(category.trim());
        p.setImageUrl(imageUrl != null ? imageUrl.trim() : "");

        return productDAO.create(p);
    }

    public boolean updateProduct(Long id, Long sellerId, String name, String description, BigDecimal price, Integer stock, String category, String imageUrl) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name is required.");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Product price must be greater than zero.");
        }
        if (stock == null || stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }

        Product p = new Product();
        p.setId(id);
        p.setSellerId(sellerId);
        p.setName(name.trim());
        p.setDescription(description != null ? description.trim() : "");
        p.setPrice(price);
        p.setStock(stock);
        p.setCategory(category.trim());
        p.setImageUrl(imageUrl != null ? imageUrl.trim() : "");

        return productDAO.update(p);
    }

    public boolean deleteProduct(Long id, Long sellerId) {
        return productDAO.delete(id, sellerId);
    }

    public int getTotalProductsCount() {
        return productDAO.countAll();
    }

    public int getSellerProductsCount(Long sellerId) {
        return productDAO.countBySeller(sellerId);
    }
}
