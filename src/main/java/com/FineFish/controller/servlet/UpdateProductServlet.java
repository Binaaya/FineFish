package com.FineFish.controller.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.FineFish.controller.dao.CategoryDAO;
import com.FineFish.controller.dao.ProductDAO;
import com.FineFish.model.Category;
import com.FineFish.model.Products;
import com.FineFish.model.User;

/**
 * Servlet implementation class UpdateProductServlet
 * Handles updating existing products in the database
 */
@WebServlet("/UpdateProductServlet")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,    // 2MB
    maxFileSize = 1024 * 1024 * 10,         // 10MB
    maxRequestSize = 1024 * 1024 * 50       // 50MB
)
public class UpdateProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * Default constructor
     */
    public UpdateProductServlet() {
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
            // Get all categories for dropdown
            CategoryDAO categoryDAO = new CategoryDAO();
            List<Category> categories = categoryDAO.getAllCategories();
            request.setAttribute("categories", categories);
            
            // Check if productId is provided
            String productIdStr = request.getParameter("productId");
            
            // Get search parameter if available
            String searchTerm = request.getParameter("search");
            
            // Create ProductDAO instance
            ProductDAO productDAO = new ProductDAO();
            
            // Declare products list
            List<Products> products;
            
            if (productIdStr != null && !productIdStr.trim().isEmpty()) {
                // Get the product to update
                try {
                    int productId = Integer.parseInt(productIdStr);
                    Products product = productDAO.getProductById(productId);
                    
                    if (product != null) {
                        request.setAttribute("product", product);
                    } else {
                        request.setAttribute("errorMessage", "Product not found");
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "Invalid product ID format");
                }
            } else if (searchTerm == null && "admin".equals(request.getParameter("destination"))) {
                // If no product ID is provided and no search term, but destination is admin, 
                // redirect to product listing
                request.getRequestDispatcher("/GetCategoriesServlet?destination=admin").forward(request, response);
                return;
            }
            
            // Get products based on search term if provided
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                // Search for products by name or ID
                products = productDAO.searchProductsByNameOrId(searchTerm);
            } else {
                // If no search term, get all products
                products = productDAO.getAllProducts();
            }
            
            // Set products in request attribute
            request.setAttribute("products", products);
            
            // Forward to update product page
            request.getRequestDispatcher("/pages/admin/UpdateProduct.jsp").forward(request, response);
            
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error occurred. Please try again later.");
            request.getRequestDispatcher("/pages/admin/Dashboard.jsp").forward(request, response);
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
        
        // Get form parameters
        String productIdStr = request.getParameter("productId");
        String productName = request.getParameter("productName");
        String categoryIdStr = request.getParameter("categoryId");
        String priceStr = request.getParameter("price");
        String quantityStr = request.getParameter("quantity");
        String speciesDescription = request.getParameter("speciesDescription");
        
        // Validate inputs
        if (productIdStr == null || productIdStr.trim().isEmpty()) {
            handleError(request, response, "Product ID is required");
            return;
        }
        
        if (productName == null || productName.trim().isEmpty()) {
            handleError(request, response, "Product name is required");
            return;
        }
        
        if (categoryIdStr == null || categoryIdStr.trim().isEmpty()) {
            handleError(request, response, "Category is required");
            return;
        }
        
        if (priceStr == null || priceStr.trim().isEmpty()) {
            handleError(request, response, "Price is required");
            return;
        }
        
        if (quantityStr == null || quantityStr.trim().isEmpty()) {
            handleError(request, response, "Quantity is required");
            return;
        }
        
        if (speciesDescription == null || speciesDescription.trim().isEmpty()) {
            handleError(request, response, "Species description is required");
            return;
        }
        
        try {
            // Parse numeric values
            int productId = Integer.parseInt(productIdStr);
            int categoryId = Integer.parseInt(categoryIdStr);
            BigDecimal price = new BigDecimal(priceStr);
            int quantity = Integer.parseInt(quantityStr);
            
            if (quantity < 0) {
                handleError(request, response, "Quantity cannot be negative");
                return;
            }
            
            // Create ProductDAO and check if product exists
            ProductDAO productDAO = new ProductDAO();
            Products existingProduct = productDAO.getProductById(productId);
            
            if (existingProduct == null) {
                handleError(request, response, "Product with ID " + productId + " does not exist");
                return;
            }
            
            // Create product object with updated values
            Products product = new Products();
            product.setId(productId);
            product.setName(productName);
            product.setCategoryId(categoryId);
            product.setPrice(price);
            product.setQuantity(quantity);
            product.setDescription(speciesDescription);
            
            // Keep existing photo if no new one is uploaded
            product.setPhoto(existingProduct.getPhoto());
            
            // Handle file upload
            Part filePart = request.getPart("productImage");
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = getSubmittedFileName(filePart);
                
                // Update photo path
                product.setPhoto(fileName);
                
                // Save file
                String uploadPath = getServletContext().getRealPath("/images/products");
                filePart.write(uploadPath + "/" + fileName);
            }
            
            // Update product in database
            int result = productDAO.updateProduct(product);
            
            if (result > 0) {
                // Success
                request.getSession().setAttribute("successMessage", "Product updated successfully!");
            } else {
                // Failed
                request.getSession().setAttribute("errorMessage", "Failed to update product. Please try again.");
            }
            
            // Redirect to product listing
            response.sendRedirect(request.getContextPath() + "/GetCategoriesServlet?destination=admin");
            
        } catch (NumberFormatException e) {
            handleError(request, response, "Invalid numeric input: " + e.getMessage());
        } catch (SQLException | ClassNotFoundException e) {
            handleError(request, response, "Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Helper method to handle errors
     */
    private void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage) 
            throws ServletException, IOException {
        
        try {
            // Get categories for form again
            CategoryDAO categoryDAO = new CategoryDAO();
            List<Category> categories = categoryDAO.getAllCategories();
            request.setAttribute("categories", categories);
            
            // If there's a product ID, get the product again
            String productIdStr = request.getParameter("productId");
            if (productIdStr != null && !productIdStr.trim().isEmpty()) {
                try {
                    int productId = Integer.parseInt(productIdStr);
                    ProductDAO productDAO = new ProductDAO();
                    Products product = productDAO.getProductById(productId);
                    
                    if (product != null) {
                        request.setAttribute("product", product);
                    }
                } catch (Exception e) {
                    // If product can't be loaded, just continue
                    System.err.println("Error loading product: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            // If categories can't be loaded, just continue
            System.err.println("Error loading categories: " + e.getMessage());
        }
        
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("/pages/admin/UpdateProduct.jsp").forward(request, response);
    }
    
    /**
     * Helper method to get the file name from the part
     */
    private String getSubmittedFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}