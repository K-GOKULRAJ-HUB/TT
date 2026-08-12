package com.gokulmart.service;

import com.gokulmart.dao.UserDAO;
import com.gokulmart.model.Role;
import com.gokulmart.model.User;
import com.gokulmart.util.PasswordUtil;

import java.util.Optional;

public class AuthService {

    private final UserDAO userDAO = new UserDAO();

    public Optional<User> authenticate(String email, String plainTextPassword) {
        if (email == null || plainTextPassword == null || email.trim().isEmpty() || plainTextPassword.trim().isEmpty()) {
            return Optional.empty();
        }

        Optional<User> userOpt = userDAO.findByEmail(email.trim());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (PasswordUtil.checkPassword(plainTextPassword, user.getPasswordHash())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public User registerUser(String name, String email, String plainTextPassword, Role role) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required.");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Valid email is required.");
        }
        if (plainTextPassword == null || plainTextPassword.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        }
        if (role == Role.ADMIN) {
            throw new IllegalArgumentException("Admin users cannot be registered publicly.");
        }
        if (userDAO.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("An account with this email already exists.");
        }

        String passwordHash = PasswordUtil.hashPassword(plainTextPassword);
        User user = new User();
        user.setName(name.trim());
        user.setEmail(email.trim().toLowerCase());
        user.setPasswordHash(passwordHash);
        user.setRole(role);

        return userDAO.create(user);
    }
}
