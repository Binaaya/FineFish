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
 * Servlet implementation class DeleteUserServlet
 * Handles users deleting their own accounts only
 */
@WebServlet("/DeleteUserServlet")
public class DeleteUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * Default constructor
     */
    public DeleteUserServlet() {
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
        
        // Only allow users to delete their own account - show confirmation page
        request.getRequestDispatcher("/pages/user/DeleteAccount.jsp").forward(request, response);
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
        
        // Get confirmation
        String confirmation = request.getParameter("confirmation");
        
        if (!"DELETE".equals(confirmation)) {
            request.setAttribute("errorMessage", "Please type DELETE to confirm account deletion");
            request.getRequestDispatcher("/pages/user/DeleteAccount.jsp").forward(request, response);
            return;
        }
        
        try {
            UserDAO userDao = new UserDAO();
            
            // Delete user's own account only
            boolean deleteSuccess = userDao.deleteUser(user.getUserId());
            
            if (deleteSuccess) {
                // Invalidate session
                session.invalidate();
                
                // Create a new session and store temporary attribute
                HttpSession newSession = request.getSession(true);
                newSession.setAttribute("accountDeleted", "true");
                
                // Redirect to login page
                response.sendRedirect(request.getContextPath() + "/pages/user/Login.jsp");
            } else {
                request.setAttribute("errorMessage", "Failed to delete your account. Please try again later");
                request.getRequestDispatcher("/pages/user/DeleteAccount.jsp").forward(request, response);
            }
            
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error deleting user account: " + e.getMessage());
            e.printStackTrace();
            
            request.setAttribute("errorMessage", "A system error occurred. Please try again later");
            request.getRequestDispatcher("/pages/user/DeleteAccount.jsp").forward(request, response);
        }
    }
}