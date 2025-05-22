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

/**
 * Servlet implementation class AddProductServlet
 * Handles adding a new product to the database
 */
@WebServlet("/AddProductServlet")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,    // 2MB
    maxFileSize = 1024 * 1024 * 10,         // 10MB
    maxRequestSize = 1024 * 1024 * 50       // 50MB
)
public class AddProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * Default constructor
     */
    public AddProductServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // Get all categories for dropdown
            CategoryDAO categoryDAO = new CategoryDAO();
            List<Category> categories = categoryDAO.getAllCategories();
            request.setAttribute("categories", categories);
            
            // Forward to add product page
            request.getRequestDispatcher("/pages/admin/AddProduct.jsp").forward(request, response);
            
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error retrieving categories: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error loading categories");
            request.getRequestDispatcher("/pages/admin/AddProduct.jsp").forward(request, response);
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Get form parameters
        String productName = request.getParameter("productName");
        String categoryIdStr = request.getParameter("categoryId");
        String priceStr = request.getParameter("price");
        String quantityStr = request.getParameter("quantity");
        String speciesDescription = request.getParameter("speciesDescription"); // New parameter
        
        // Validate inputs
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
        
        // Validate description
        if (speciesDescription == null || speciesDescription.trim().isEmpty()) {
            handleError(request, response, "Product description is required");
            return;
        }
        
        try {
            // Parse numeric values
            int categoryId = Integer.parseInt(categoryIdStr);
            BigDecimal price = new BigDecimal(priceStr);
            int quantity = Integer.parseInt(quantityStr);
            
            if (quantity < 0) {
                handleError(request, response, "Quantity cannot be negative");
                return;
            }
            
            // Create product object
            Products product = new Products();
            product.setName(productName);
            product.setCategoryId(categoryId);
            product.setPrice(price);
            product.setQuantity(quantity);
            product.setDescription(speciesDescription); // Set description
            
         // Handle file upload
            Part filePart = request.getPart("productImage");
            String fileName = null;

            if (filePart == null || filePart.getSize() <= 0) {
                handleError(request, response, "Product image is required");
                return;
            }

            fileName = getSubmittedFileName(filePart);

            // Set image path
            product.setPhoto(fileName);

            // Save file
            String uploadPath = getServletContext().getRealPath("/images/products");
            filePart.write(uploadPath + "/" + fileName);
            
            // Get category name for success message
            CategoryDAO categoryDAO = new CategoryDAO();
            Category category = categoryDAO.getCategoryById(categoryId);
            String categoryName = (category != null) ? category.getName() : "Unknown Category";
            
            // Add product to database
            ProductDAO productDAO = new ProductDAO();
            int result = productDAO.addProduct(product);
            
            if (result > 0) {
                // Get the ID of the newly added product
                int newProductId = productDAO.getLastInsertedProductId();
                
                // Success with detailed message
                String successMessage = "Product '" + productName + "' (ID: " + newProductId + ") has been added successfully";
                successMessage += " in category '" + categoryName + "' with price $" + price + " and quantity: " + quantity;
                if (fileName != null) {
                    successMessage += " and image: " + fileName;
                }
                
                request.getSession().setAttribute("successMessage", successMessage);
                response.sendRedirect(request.getContextPath() + "/AddProductServlet");
                return;
            } else {
                // Failed
                request.setAttribute("errorMessage", "Failed to add product. Please try again.");
            }
            
            // If we get here, something went wrong. Get categories for form again
            List<Category> categories = categoryDAO.getAllCategories();
            request.setAttribute("categories", categories);
            
            // Forward back to add product page
            request.getRequestDispatcher("/pages/admin/AddProduct.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            handleError(request, response, "Invalid numeric input: " + e.getMessage());
        } catch (Exception e) {
            handleError(request, response, "Error adding product: " + e.getMessage());
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
        } catch (Exception e) {
            // If categories can't be loaded, just continue
            System.err.println("Error loading categories: " + e.getMessage());
        }
        
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("/pages/admin/AddProduct.jsp").forward(request, response);
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