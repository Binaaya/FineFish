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

/**
 * Servlet implementation class GetUserServlet
 * Handles retrieving and displaying a single user's details
 */
@WebServlet("/GetUserServlet")
public class GetUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * Default constructor
     */
    public GetUserServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null) {
            // User not logged in, redirect to login page
            response.sendRedirect(request.getContextPath() + "/pages/user/Login.jsp");
            return;
        }
        
        // Get user ID from request parameter
        String userIdStr = request.getParameter("userId");
        
        // If userId is not provided, use the current user's ID
        int userId;
        if (userIdStr == null || userIdStr.trim().isEmpty()) {
            userId = currentUser.getUserId();
        } else {
            try {
                userId = Integer.parseInt(userIdStr);
                
                // Check permissions - only admins can view other users' details
                if (userId != currentUser.getUserId() && !"admin".equals(currentUser.getRole())) {
                    session.setAttribute("errorMessage", "Access denied. You don't have permission to view this user's details");
                    response.sendRedirect(request.getContextPath() + "/pages/user/Dashboard.jsp");
                    return;
                }
            } catch (NumberFormatException e) {
                session.setAttribute("errorMessage", "Invalid user ID format");
                response.sendRedirect(request.getContextPath() + "/pages/user/Dashboard.jsp");
                return;
            }
        }
        
        try {
            UserDAO userDao = new UserDAO();
            
            // Get user by ID
            User user = userDao.getUserById(userId);
            
            if (user == null) {
                session.setAttribute("errorMessage", "User not found");
                
                // Redirect based on user role
                if ("admin".equals(currentUser.getRole())) {
                    response.sendRedirect(request.getContextPath() + "/ListUsersServlet");
                } else {
                    response.sendRedirect(request.getContextPath() + "/pages/user/Dashboard.jsp");
                }
                return;
            }
            
            // Set user in request attribute
            request.setAttribute("displayUser", user);
            
            // Forward to appropriate page based on role
            if ("admin".equals(currentUser.getRole()) && userId != currentUser.getUserId()) {
                // Admin viewing another user
                request.getRequestDispatcher("/pages/admin/UserDetails.jsp").forward(request, response);
            } else {
                // User viewing their own details or admin viewing their own details
                request.getRequestDispatcher("/pages/user/Dashboard.jsp").forward(request, response);
            }
            
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error retrieving user: " + e.getMessage());
            e.printStackTrace();
            
            session.setAttribute("errorMessage", "A system error occurred. Please try again later");
            
            // Redirect based on user role
            if ("admin".equals(currentUser.getRole())) {
                response.sendRedirect(request.getContextPath() + "/ListUsersServlet");
            } else {
                response.sendRedirect(request.getContextPath() + "/pages/user/Dashboard.jsp");
            }
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Post requests redirect to get
        doGet(request, response);
    }
}