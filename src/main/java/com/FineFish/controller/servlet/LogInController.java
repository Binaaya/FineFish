package com.FineFish.controller.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Cookie;

import com.FineFish.controller.dao.UserDAO;
import com.FineFish.model.User;

/**
 * Servlet implementation class LogInController
 * Handles user authentication and login functionality
 */

@WebServlet(asyncSupported = true, urlPatterns = { "/LogInController" })
public class LogInController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * Default constructor
     */
    public LogInController() {
        super();
    }

    /**
     * This method runs when the user accesses /LogInController with GET (e.g., clicking a login link)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         // Check if there's an existing session (false = don't create new)
        HttpSession session = request.getSession(false);
        
        // If session exists and has a "user" attribute, the user is already logged in
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            // Redirect admin users to admin dashboard
            if ("admin".equals(user.getRole())) {
                response.sendRedirect(request.getContextPath() + "/AdminDashboardServlet");
            } else {
                // Redirect normal users to homepage
                response.sendRedirect(request.getContextPath() + "/index.jsp");
            }
            return;
        }
        
        //  If user is not logged in, forward them to the login page
        request.getRequestDispatcher("/pages/user/Login.jsp").forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    // This method runs when the user submits the login form (POST request)
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get login credentials from form/Get login form values from request
        String usernameOrEmail = request.getParameter("usernameOrEmail");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");
        
        // Validate input: check if username/email or password is empty
        if (usernameOrEmail == null || usernameOrEmail.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
        	
        	// If any field is empty, send error message back to login page
            request.setAttribute("errorMessage", "Username/Email and password are required");
            request.getRequestDispatcher("/pages/user/Login.jsp").forward(request, response);
            return;
        }
        
        try {
            // Attempt to authenticate user/Create UserDAO object to interact with database
            UserDAO userDao = new UserDAO();
            
            // Call method to check if user exists with given credentials
            User user = userDao.authenticateUser(usernameOrEmail, password);
            
            // If user is found and authenticated
            if (user != null) {
                // Create session for the logged-in user/Start a new session and store the user object
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setMaxInactiveInterval(30 * 60); // 30 minutes session timeout
                
                // Handle "Remember Me" functionality
                if ("yes".equals(rememberMe)) {
                	
                	// If checked, create a cookie to remember username/email
                    Cookie userCookie = new Cookie("user_cookie", usernameOrEmail);
                    userCookie.setMaxAge(30 * 24 * 60 * 60); // 30 days
                    response.addCookie(userCookie);
                } else {
                	// If unchecked, delete existing cookie by setting max age to 0
                    Cookie userCookie = new Cookie("user_cookie", "");
                    userCookie.setMaxAge(0);
                    response.addCookie(userCookie);
                }
                
                // Redirect based on user role
                if ("admin".equals(user.getRole())) {
                    response.sendRedirect(request.getContextPath() + "/AdminDashboardServlet");
                } else {
                    response.sendRedirect(request.getContextPath() + "/index.jsp");
                }
            } else {
                // Authentication failed, show error message
                request.setAttribute("errorMessage", "Invalid username/email or password");
                request.getRequestDispatcher("/pages/user/Login.jsp").forward(request, response);
            }
        } catch (SQLException | ClassNotFoundException e) {
        	// If there's a database or class loading error, print it
            System.err.println("Error during login: " + e.getMessage());
            e.printStackTrace();
            
            // Show a general error message to the user,Send error message to login page
            request.setAttribute("errorMessage", "A system error occurred. Please try again later.");
            request.getRequestDispatcher("/pages/user/Login.jsp").forward(request, response);
        }
    }
}