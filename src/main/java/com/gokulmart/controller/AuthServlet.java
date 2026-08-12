package com.gokulmart.controller;

import com.gokulmart.model.Role;
import com.gokulmart.model.User;
import com.gokulmart.service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@WebServlet(urlPatterns = {"/login", "/register", "/logout"})
public class AuthServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/logout".equals(path)) {
            HttpSession session = req.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            String msg = URLEncoder.encode("You have been logged out successfully.", StandardCharsets.UTF_8);
            resp.sendRedirect(req.getContextPath() + "/login?success=" + msg);
            return;
        }

        if ("/login".equals(path)) {
            req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
        } else if ("/register".equals(path)) {
            req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/login".equals(path)) {
            handleLogin(req, resp);
        } else if ("/register".equals(path)) {
            handleRegister(req, resp);
        }
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        Optional<User> userOpt = authService.authenticate(email, password);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            HttpSession session = req.getSession(true);
            session.setAttribute("user", user);

            // Redirect based on role
            if (user.getRole() == Role.ADMIN) {
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
            } else if (user.getRole() == Role.SELLER) {
                resp.sendRedirect(req.getContextPath() + "/seller/dashboard");
            } else {
                resp.sendRedirect(req.getContextPath() + "/home");
            }
        } else {
            req.setAttribute("error", "Invalid email address or password.");
            req.setAttribute("email", email);
            req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
        }
    }

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        String roleStr = req.getParameter("role");

        if (password == null || !password.equals(confirmPassword)) {
            req.setAttribute("error", "Passwords do not match.");
            req.setAttribute("name", name);
            req.setAttribute("email", email);
            req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, resp);
            return;
        }

        try {
            Role role = Role.BUYER;
            if ("SELLER".equalsIgnoreCase(roleStr)) {
                role = Role.SELLER;
            }

            User user = authService.registerUser(name, email, password, role);
            HttpSession session = req.getSession(true);
            session.setAttribute("user", user);

            String successMsg = URLEncoder.encode("Account registered successfully! Welcome to Gokul Mart.", StandardCharsets.UTF_8);
            if (role == Role.SELLER) {
                resp.sendRedirect(req.getContextPath() + "/seller/dashboard?success=" + successMsg);
            } else {
                resp.sendRedirect(req.getContextPath() + "/home?success=" + successMsg);
            }
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("name", name);
            req.setAttribute("email", email);
            req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, resp);
        }
    }
}
