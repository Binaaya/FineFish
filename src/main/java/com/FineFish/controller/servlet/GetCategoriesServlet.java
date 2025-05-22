package com.FineFish.controller.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.FineFish.controller.dao.CategoryDAO;
import com.FineFish.controller.dao.ProductDAO;
import com.FineFish.model.Category;
import com.FineFish.model.Products;

/**
 * Servlet implementation class GetCategoriesServlet
 * Handles retrieving categories and products for display
 */
@WebServlet("/GetCategoriesServlet")
public class GetCategoriesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * Default constructor
     */
    public GetCategoriesServlet() {
        super();
    }
    
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // Create CategoryDAO
            CategoryDAO categoryDAO = new CategoryDAO();
            
            // Get all categories
            List<Category> categories = categoryDAO.getAllCategories();
            
            // Set the list in request attribute
            request.setAttribute("categories", categories);
            
            // Get total product count for "All Products" category
            int totalProductCount = categoryDAO.getTotalProductCount();
            request.setAttribute("totalProductCount", totalProductCount);
            
            // Check if this is a request for a specific category
            String categoryIdStr = request.getParameter("categoryId");
            
            // Create ProductDAO for retrieving products
            ProductDAO productDAO = new ProductDAO();
            List<Products> products;
            
            // If categoryId is provided, get products for that category
            if (categoryIdStr != null && !categoryIdStr.trim().isEmpty()) {
                try {
                    int categoryId = Integer.parseInt(categoryIdStr);
                    
                    // Get products for this category
                    products = productDAO.getProductsByCategory(categoryId);
                    
                    // Get category name for the title
                    Category category = categoryDAO.getCategoryById(categoryId);
                    if (category != null) {
                        request.setAttribute("categoryName", category.getName());
                    }
                } catch (NumberFormatException e) {
                    // Invalid categoryId, get all products instead
                    System.err.println("Invalid category ID: " + e.getMessage());
                    products = productDAO.getAllProducts();
                }
            } else {
                // No category specified, get all products
                products = productDAO.getAllProducts();
            }
            
            // Apply sorting if specified
            String sortOption = request.getParameter("sort");
            if (sortOption != null && !sortOption.trim().isEmpty()) {
                sortProducts(products, sortOption);
            }
            
            // Set the products in request attribute
            request.setAttribute("products", products);
            
            // Determine where to forward the request
            String destination = request.getParameter("destination");
            
            if ("admin".equals(destination)) {
                // Forward to admin view
                request.getRequestDispatcher("/pages/admin/UpdateProduct.jsp").forward(request, response);
            } else {
                // Forward to user view
                request.getRequestDispatcher("/pages/user/Product.jsp").forward(request, response);
            }
            
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error retrieving data: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error retrieving data from database");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }
    
    /**
     * Sort the products list based on the provided sort option
     * 
     * @param products List of products to sort
     * @param sortOption Sort option (price-asc, price-desc, newest)
     */
    private void sortProducts(List<Products> products, String sortOption) {
        switch (sortOption) {
            case "price-asc":
                // Sort by price in ascending order
                Collections.sort(products, new java.util.Comparator<Products>() {
                    @Override
                    public int compare(Products p1, Products p2) {
                        return p1.getPrice().compareTo(p2.getPrice());
                    }
                });
                break;
            case "price-desc":
                // Sort by price in descending order
                Collections.sort(products, new java.util.Comparator<Products>() {
                    @Override
                    public int compare(Products p1, Products p2) {
                        return p2.getPrice().compareTo(p1.getPrice());
                    }
                });
                break;
            case "newest":
                // Since we don't have a date field, we'll sort by ID assuming newer products have higher IDs
                Collections.sort(products, new java.util.Comparator<Products>() {
                    @Override
                    public int compare(Products p1, Products p2) {
                        return Integer.compare(p2.getId(), p1.getId());
                    }
                });
                break;
            default:
                // Default sorting (featured) - no special sorting
                break;
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
