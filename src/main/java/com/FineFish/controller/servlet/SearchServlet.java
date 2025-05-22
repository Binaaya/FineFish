package com.FineFish.controller.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.FineFish.controller.dao.ProductDAO;
import com.FineFish.controller.dao.CategoryDAO;
import com.FineFish.model.Products;
import com.FineFish.model.Category;

/**
 * Servlet implementation class SearchServlet
 * Handles searching for products from any page
 */
@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * Default constructor
     */
    public SearchServlet() {
        super();
    }
    
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // Get search term from request
            String searchTerm = request.getParameter("searchTerm");
            
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                // Create ProductDAO for retrieving products
                ProductDAO productDAO = new ProductDAO();
                
                // Search for products matching the search term
                List<Products> products = productDAO.searchProducts(searchTerm);
                
                // Set the products in request attribute
                request.setAttribute("products", products);
                request.setAttribute("searchTerm", searchTerm);
                
                // Get categories for sidebar
                CategoryDAO categoryDAO = new CategoryDAO();
                List<Category> categories = categoryDAO.getAllCategories();
                request.setAttribute("categories", categories);
                
                // Get total product count for "All Products" category
                int totalProductCount = categoryDAO.getTotalProductCount();
                request.setAttribute("totalProductCount", totalProductCount);
                
                // Add a status message if no products found
                if (products.isEmpty()) {
                    request.setAttribute("message", "No products found matching '" + searchTerm + "'");
                    request.setAttribute("messageType", "info");
                }
            } else {
                // If search term is empty, redirect to the products page
                response.sendRedirect(request.getContextPath() + "/GetCategoriesServlet");
                return;
            }
            
            // Forward to the product page
            request.getRequestDispatcher("/pages/user/Product.jsp").forward(request, response);
            
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error searching data: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error searching products");
            request.getRequestDispatcher("/pages/user/Product.jsp").forward(request, response);
        }
    }
    
    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}