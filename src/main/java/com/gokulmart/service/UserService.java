package com.gokulmart.service;

import com.gokulmart.dao.UserDAO;
import com.gokulmart.model.Role;
import com.gokulmart.model.User;

import java.util.List;
import java.util.Optional;

public class UserService {

    private final UserDAO userDAO = new UserDAO();

    public Optional<User> getUserById(Long id) {
        return userDAO.findById(id);
    }

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public int getTotalUsersCount() {
        return userDAO.countAll();
    }

    public int getUsersByRoleCount(Role role) {
        return userDAO.countByRole(role);
    }
}
