package com.gokulmart.service;

import com.gokulmart.dao.WishlistDAO;
import com.gokulmart.model.Product;

import java.util.List;
import java.util.Set;

public class WishlistService {
    private final WishlistDAO wishlistDAO;

    public WishlistService() {
        this.wishlistDAO = new WishlistDAO();
    }

    public boolean addProductToWishlist(long userId, long productId) {
        return wishlistDAO.addProductToWishlist(userId, productId);
    }

    public boolean removeProductFromWishlist(long userId, long productId) {
        return wishlistDAO.removeProductFromWishlist(userId, productId);
    }

    public List<Product> getWishlistByUser(long userId) {
        return wishlistDAO.getWishlistByUser(userId);
    }
    
    public Set<Long> getWishlistedProductIdsByUser(long userId) {
        return wishlistDAO.getWishlistedProductIdsByUser(userId);
    }
}
