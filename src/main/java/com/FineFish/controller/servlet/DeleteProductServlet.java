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
import com.FineFish.model.Products;
import com.FineFish.model.User;

/**
 * Servlet implementation class DeleteProductServlet
 * Handles deleting products from the database and displaying products for deletion
 */
@WebServlet("/DeleteProductServlet")
public class DeleteProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * Default constructor
     */
    public DeleteProductServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check if user is admin
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/pages/user/Login.jsp");
            return;
        }
        
        try {
            // Create ProductDAO
            ProductDAO productDAO = new ProductDAO();
            
            // Get the product ID parameter (if provided)
            String productIdStr = request.getParameter("productId");
            String viewOnly = request.getParameter("view"); // flag to indicate view-only mode
            String searchTerm = request.getParameter("search"); // search term for filtering products
            
            // If product ID is provided and it's not just for viewing
            if (productIdStr != null && !productIdStr.trim().isEmpty() && viewOnly == null) {
                // This is a delete action
                try {
                    int productId = Integer.parseInt(productIdStr);
                    
                    // Delete the product
                    int result = productDAO.deleteProduct(productId);
                    
                    if (result > 0) {
                        // Success
                        request.getSession().setAttribute("successMessage", "Product deleted successfully!");
                    } else {
                        // Failed
                        request.getSession().setAttribute("errorMessage", "Failed to delete product. Please try again.");
                    }
                    
                    // Redirect back to the delete product page
                    response.sendRedirect(request.getContextPath() + "/DeleteProductServlet");
                    return;
                    
                } catch (NumberFormatException e) {
                    // Invalid product ID format
                    request.setAttribute("errorMessage", "Invalid product ID format");
                }
            } 
            // If product ID is provided and it's for viewing
            else if (productIdStr != null && !productIdStr.trim().isEmpty() && "true".equals(viewOnly)) {
                // Load the product details for viewing
                try {
                    int productId = Integer.parseInt(productIdStr);
                    Products product = productDAO.getProductById(productId);
                    
                    if (product != null) {
                        request.setAttribute("product", product);
                    } else {
                        request.setAttribute("errorMessage", "Product not found");
                    }
                } catch (NumberFormatException e) {
                    // Invalid product ID format
                    request.setAttribute("errorMessage", "Invalid product ID format");
                }
            }
            
            // Get products for display (filtered by search term if provided)
            List<Products> products;
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                // Search for products by name or ID
                products = productDAO.searchProductsByNameOrId(searchTerm);
            } else {
                // Get all products
                products = productDAO.getAllProducts();
            }
            
            // Set products in request
            request.setAttribute("products", products);
            
            // Forward to the delete product page
            request.getRequestDispatcher("/pages/admin/DeleteProduct.jsp").forward(request, response);
            
        } catch (SQLException | ClassNotFoundException e) {
            // Database error
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            e.printStackTrace();
            request.getRequestDispatcher("/pages/admin/DeleteProduct.jsp").forward(request, response);
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check if user is admin
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/pages/user/Login.jsp");
            return;
        }
        
        // Get the product ID to delete
        String productIdStr = request.getParameter("productId");
        
        // Validate input
        if (productIdStr == null || productIdStr.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Product ID is required");
            request.getRequestDispatcher("/pages/admin/DeleteProduct.jsp").forward(request, response);
            return;
        }
        
        try {
            // Parse product ID
            int productId = Integer.parseInt(productIdStr);
            
            // Create ProductDAO and delete the product
            ProductDAO productDAO = new ProductDAO();
            
            // Check if product exists
            if (!productDAO.productExists(productId)) {
                request.setAttribute("errorMessage", "Product with ID " + productId + " does not exist");
                request.getRequestDispatcher("/pages/admin/DeleteProduct.jsp").forward(request, response);
                return;
            }
            
            // Delete the product
            int result = productDAO.deleteProduct(productId);
            
            if (result > 0) {
                // Success
                request.getSession().setAttribute("successMessage", "Product deleted successfully!");
                response.sendRedirect(request.getContextPath() + "/GetCategoriesServlet?destination=admin");
            } else {
                // Failed
                request.setAttribute("errorMessage", "Failed to delete product. Please try again.");
                request.getRequestDispatcher("/pages/admin/DeleteProduct.jsp").forward(request, response);
            }
            
        } catch (NumberFormatException e) {
            // Invalid product ID
            request.setAttribute("errorMessage", "Invalid product ID format");
            request.getRequestDispatcher("/pages/admin/DeleteProduct.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException e) {
            // Database error
            request.setAttribute("errorMessage", "Error deleting product: " + e.getMessage());
            e.printStackTrace();
            request.getRequestDispatcher("/pages/admin/DeleteProduct.jsp").forward(request, response);
        }
    }
}