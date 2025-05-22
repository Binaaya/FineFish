package com.FineFish.controller.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.FineFish.controller.dao.CategoryDAO;
import com.FineFish.model.Category;

/**
 * Servlet implementation class AddCategoryServlet
 * Handles adding a new category to the database
 */
@WebServlet(name = "AddCategoryServlet", urlPatterns = { "/AddCategoryServlet" })
public class AddCategoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * Default constructor
     */
    public AddCategoryServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        System.out.println("AddCategoryServlet: doGet method called");
        // Forward to add category page
        request.getRequestDispatcher("/pages/admin/AddCategory.jsp").forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        System.out.println("AddCategoryServlet: doPost method called");
        
        // Get form parameters
        String categoryIdStr = request.getParameter("categoryId");
        String categoryName = request.getParameter("categoryName");
        
        System.out.println("Received categoryId: " + categoryIdStr);
        System.out.println("Received categoryName: " + categoryName);
        
        // Validate inputs on server side as well
        if (categoryIdStr == null || categoryIdStr.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Category ID is required");
            request.getRequestDispatcher("/pages/admin/AddCategory.jsp").forward(request, response);
            return;
        }
        
        if (categoryName == null || categoryName.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Category Name is required");
            request.getRequestDispatcher("/pages/admin/AddCategory.jsp").forward(request, response);
            return;
        }
        
        // Check if Category ID is a number
        int categoryId;
        try {
            categoryId = Integer.parseInt(categoryIdStr);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing category ID: " + e.getMessage());
            request.setAttribute("errorMessage", "Category ID must be a number");
            request.getRequestDispatcher("/pages/admin/AddCategory.jsp").forward(request, response);
            return;
        }
        
        try {
            // Create the CategoryDAO instance
            CategoryDAO categoryDAO = new CategoryDAO();
            
            // Check if a category with this ID already exists
            if (categoryDAO.categoryExists(categoryId)) {
                request.setAttribute("errorMessage", "A category with this ID already exists");
                request.getRequestDispatcher("/pages/admin/AddCategory.jsp").forward(request, response);
                return;
            }
            
            // Create a new Category object
            Category category = new Category();
            category.setId(categoryId);
            category.setName(categoryName);
            
            // Add category to database
            int result = categoryDAO.addCategory(category);
            
            if (result > 0) {
                // Success - set success message with specific category information
                request.getSession().setAttribute("successMessage", "Category with ID: " + categoryId + " and name: '" + categoryName + "' has been added successfully!");
                response.sendRedirect(request.getContextPath() + "/AddCategoryServlet");
            } else {
                // Failed to add category
                request.setAttribute("errorMessage", "Failed to add category. Please try again.");
                request.getRequestDispatcher("/AddCategoryServlet").forward(request, response);
            }
            
        } catch (SQLException e) {
            // Handle SQL exceptions
            System.err.println("Database error when adding category: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
            request.setAttribute("errorMessage", "Database error occurred. Please try again later.");
            request.getRequestDispatcher("/AddCategoryServlet").forward(request, response);
        } catch (ClassNotFoundException e) {
            // Handle database connection exceptions
            System.err.println("Database connection error: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
            request.setAttribute("errorMessage", "Database connection error. Please try again later.");
            request.getRequestDispatcher("/AddCategoryServlet").forward(request, response);
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            System.err.println("Unexpected error adding category: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
            request.setAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
            request.getRequestDispatcher("/AddCategoryServlet").forward(request, response);
        }
    }
}