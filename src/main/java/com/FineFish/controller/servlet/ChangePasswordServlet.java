package com.FineFish.controller.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.FineFish.controller.dao.UserDAO;
import com.FineFish.model.User;
import com.FineFish.utility.EncryptDecrypt;

/**
 * Servlet implementation class ChangePasswordServlet
 * Handles changing a user's password
 */
@WebServlet("/ChangePasswordServlet")
public class ChangePasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * Default constructor
     */
    public ChangePasswordServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            // User not logged in, redirect to login page
            response.sendRedirect(request.getContextPath() + "/pages/user/Login.jsp");
            return;
        }
        
        // Forward to dashboard with password section active
        response.sendRedirect(request.getContextPath() + "/pages/user/Dashboard.jsp#password-section");
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            // User not logged in, redirect to login page
            response.sendRedirect(request.getContextPath() + "/pages/user/Login.jsp");
            return;
        }
        
        // Get form parameters
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmNewPassword = request.getParameter("confirmNewPassword");
        
        // Basic validation
        if (currentPassword == null || currentPassword.trim().isEmpty() || 
            newPassword == null || newPassword.trim().isEmpty() || 
            confirmNewPassword == null || confirmNewPassword.trim().isEmpty()) {
            
            session.setAttribute("errorMessage", "All fields are required");
            response.sendRedirect(request.getContextPath() + "/pages/user/Dashboard.jsp#password-section");
            return;
        }
        
        // Validate password length
        if (newPassword.length() < 6) {
            session.setAttribute("errorMessage", "New password must be at least 6 characters");
            response.sendRedirect(request.getContextPath() + "/pages/user/Dashboard.jsp#password-section");
            return;
        }
        
        // Check if new password and confirmation match
        if (!newPassword.equals(confirmNewPassword)) {
            session.setAttribute("errorMessage", "New password and confirmation do not match");
            response.sendRedirect(request.getContextPath() + "/pages/user/Dashboard.jsp#password-section");
            return;
        }
        
        try {
            UserDAO userDao = new UserDAO();
            
            // Get current user from database to verify current password
            User dbUser = userDao.getUserById(user.getUserId());
            
            if (dbUser == null) {
                session.setAttribute("errorMessage", "User not found in database");
                response.sendRedirect(request.getContextPath() + "/pages/user/Dashboard.jsp#password-section");
                return;
            }
            
            // Verify current password
            if (!EncryptDecrypt.checkPassword(currentPassword, dbUser.getPassword())) {
                session.setAttribute("errorMessage", "Current password is incorrect");
                response.sendRedirect(request.getContextPath() + "/pages/user/Dashboard.jsp#password-section");
                return;
            }
            
            // Check if new password is same as current one
            if (currentPassword.equals(newPassword)) {
                session.setAttribute("errorMessage", "New password must be different from current password");
                response.sendRedirect(request.getContextPath() + "/pages/user/Dashboard.jsp#password-section");
                return;
            }
            
            // Update password
            boolean updateSuccess = userDao.updatePassword(user.getUserId(), newPassword);
            
            if (updateSuccess) {
                // Update the session user object if it stores password
                if (user.getPassword() != null) {
                    user.setPassword(EncryptDecrypt.encrypt(newPassword));
                    session.setAttribute("user", user);
                }
                
                session.setAttribute("successMessage", "Your password has been changed successfully");
            } else {
                session.setAttribute("errorMessage", "Failed to change password. Please try again later");
            }
            
            // Redirect back to dashboard
            response.sendRedirect(request.getContextPath() + "/pages/user/Dashboard.jsp#password-section");
            
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error changing password: " + e.getMessage());
            e.printStackTrace();
            
            session.setAttribute("errorMessage", "A system error occurred. Please try again later");
            response.sendRedirect(request.getContextPath() + "/pages/user/Dashboard.jsp#password-section");
        }
    }
}