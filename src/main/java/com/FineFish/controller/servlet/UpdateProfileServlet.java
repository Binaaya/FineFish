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
 * Servlet implementation class UpdateProfileServlet
 * Handles updating a user's profile information
 */
@WebServlet("/UpdateProfileServlet")
public class UpdateProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * Default constructor
     */
    public UpdateProfileServlet() {
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
        
        // Forward to profile page
        request.getRequestDispatcher("/pages/user/Dashboard.jsp").forward(request, response);
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
        
        // Get form parameters - using fname and surname now to match the form fields
        String fname = request.getParameter("fname");
        String surname = request.getParameter("surname");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        String address = request.getParameter("address");
        
        // Basic validation
        if (fname == null || fname.trim().isEmpty() || 
            email == null || email.trim().isEmpty()) {
            
            request.setAttribute("errorMessage", "Name and email are required fields");
            request.getRequestDispatcher("/pages/user/Dashboard.jsp").forward(request, response);
            return;
        }
        
        try {
            UserDAO userDao = new UserDAO();
            
            // Check if the new email is already in use by another user
            if (!email.equals(user.getEmail()) && userDao.isEmailExists(email)) {
                request.setAttribute("errorMessage", "Email is already in use by another account");
                request.getRequestDispatcher("/pages/user/Dashboard.jsp").forward(request, response);
                return;
            }
            
            // Update user object with individual fields
            user.setFname(fname);
            user.setSurname(surname != null ? surname : "");
            user.setEmail(email);
            user.setPhoneNumber(phoneNumber);
            user.setAddress(address);
            
            // Update user in database
            boolean updateSuccess = userDao.updateUser(user);
            
            if (updateSuccess) {
                // Update session user object
                session.setAttribute("user", user);
                
                // Set success message
                session.setAttribute("successMessage", "Your profile has been updated successfully");
            } else {
                // Set error message
                session.setAttribute("errorMessage", "Failed to update your profile. Please try again later");
            }
            
            // Redirect back to profile page to avoid form resubmission
            response.sendRedirect(request.getContextPath() + "/pages/user/Dashboard.jsp");
            
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error updating profile: " + e.getMessage());
            e.printStackTrace();
            
            // Set error message
            session.setAttribute("errorMessage", "A system error occurred. Please try again later");
            response.sendRedirect(request.getContextPath() + "/pages/user/Dashboard.jsp");
        }
    }
}