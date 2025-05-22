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
 * Servlet implementation class RegisterController
 * Handles user registration functionality
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/RegisterController" })
public class RegisterController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * Default constructor
     */
    public RegisterController() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirect to login page with register form visible
        response.sendRedirect(request.getContextPath() + "/pages/user/Login.jsp?form=register");
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get form parameters
        String username = request.getParameter("username");
        String fname = request.getParameter("fname");
        String surname = request.getParameter("surname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String address = request.getParameter("address");
        String phoneNumber = request.getParameter("phone_number");
        String terms = request.getParameter("terms");
        
        // Validate required fields
        if (username == null || username.trim().isEmpty() ||
            fname == null || fname.trim().isEmpty() ||
            surname == null || surname.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            confirmPassword == null || confirmPassword.trim().isEmpty() ||
            phoneNumber == null || phoneNumber.trim().isEmpty()) {
            
            request.setAttribute("errorMessage", "All required fields must be filled out");
            request.getRequestDispatcher("/pages/user/Login.jsp?form=register").forward(request, response);
            return;
        }
        
        // Validate username length
        if (username.length() < 4) {
            request.setAttribute("errorMessage", "Username must be at least 4 characters");
            request.getRequestDispatcher("/pages/user/Login.jsp?form=register").forward(request, response);
            return;
        }
        
        // Validate terms acceptance
        if (terms == null) {
            request.setAttribute("errorMessage", "You must accept the Terms of Service and Privacy Policy");
            request.getRequestDispatcher("/pages/user/Login.jsp?form=register").forward(request, response);
            return;
        }
        
        // Validate password match
        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match");
            request.getRequestDispatcher("/pages/user/Login.jsp?form=register").forward(request, response);
            return;
        }
        
        // Validate password length
        if (password.length() < 6) {
            request.setAttribute("errorMessage", "Password must be at least 6 characters");
            request.getRequestDispatcher("/pages/user/Login.jsp?form=register").forward(request, response);
            return;
        }
        
        // Validate phone number - must be exactly 10 digits
        String digitsOnly = phoneNumber.replaceAll("[^0-9]", "");
        if (digitsOnly.length() != 10) {
            request.setAttribute("errorMessage", "Phone number must be exactly 10 digits");
            request.getRequestDispatcher("/pages/user/Login.jsp?form=register").forward(request, response);
            return;
        }
        
        try {
            UserDAO userDao = new UserDAO();
            
            // Check if username already exists
            if (userDao.isUsernameExists(username)) {
                request.setAttribute("errorMessage", "Username already exists");
                request.getRequestDispatcher("/pages/user/Login.jsp?form=register").forward(request, response);
                return;
            }
            
            // Check if email already exists
            if (userDao.isEmailExists(email)) {
                request.setAttribute("errorMessage", "Email already exists");
                request.getRequestDispatcher("/pages/user/Login.jsp?form=register").forward(request, response);
                return;
            }
            
            // Create new user object with default user role
            User user = new User();
            user.setUsername(username);
            user.setFname(fname);
            user.setSurname(surname);
            user.setEmail(email);
            user.setPassword(password); 
            user.setAddress(address);
            user.setPhoneNumber(phoneNumber);
            user.setRole("user"); // Default role
            
            // Register user in database
            boolean registrationSuccess = userDao.registerUser(user);
            
            if (registrationSuccess) {
                // Set success message
                request.setAttribute("successMessage", "Registration successful. Please login.");
                
                // Automatically log in the user
                User registeredUser = userDao.authenticateUser(email, password);
                if (registeredUser != null) {
                    HttpSession session = request.getSession();
                    session.setAttribute("user", registeredUser);
                    session.setMaxInactiveInterval(30 * 60); // 30 minutes session timeout
                    
                    // Redirect to user dashboard
                    response.sendRedirect(request.getContextPath() + "/pages/user/Dashboard.jsp");
                    return;
                } else {
                    // Redirect to login page
                    request.getRequestDispatcher("/pages/user/Login.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("errorMessage", "Registration failed. Please try again later.");
                request.getRequestDispatcher("/pages/user/Login.jsp?form=register").forward(request, response);
            }
            
        } catch (SQLException | ClassNotFoundException e) {
            // Log the error (in production, use a proper logging framework)
            System.err.println("Error during registration: " + e.getMessage());
            e.printStackTrace();
            
            // Send error message to login page
            request.setAttribute("errorMessage", "A system error occurred. Please try again later.");
            request.getRequestDispatcher("/pages/user/Login.jsp?form=register").forward(request, response);
        }
    }
}