package com.gokulmart.service;

import com.gokulmart.dao.ReviewDAO;
import com.gokulmart.model.Review;

import java.util.List;

public class ReviewService {

    private final ReviewDAO reviewDAO = new ReviewDAO();

    public Review addReview(Long userId, Long productId, int rating, String comment) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5 stars.");
        }
        if (!reviewDAO.hasUserPurchasedProduct(userId, productId)) {
            throw new IllegalArgumentException("You can only review products you have purchased.");
        }

        Review review = new Review();
        review.setUserId(userId);
        review.setProductId(productId);
        review.setRating(rating);
        review.setComment(comment != null ? comment.trim() : "");

        return reviewDAO.create(review);
    }

    public List<Review> getProductReviews(Long productId) {
        return reviewDAO.findByProductId(productId);
    }

    public double getAverageRating(Long productId) {
        return reviewDAO.getAverageRating(productId);
    }

    public boolean canUserReview(Long userId, Long productId) {
        return reviewDAO.hasUserPurchasedProduct(userId, productId);
    }
}
