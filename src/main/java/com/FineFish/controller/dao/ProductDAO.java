package com.FineFish.controller.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.FineFish.controller.database.DatabaseConnection;
import com.FineFish.model.Products;

/**
 * Data Access Object for Products
 * Handles database operations related to products
 */
public class ProductDAO {
    
    private Connection connection;
    
    /**
     * Constructor - establishes database connection
     * 
     * @throws SQLException If a database error occurs
     * @throws ClassNotFoundException If the database driver is not found
     */
    public ProductDAO() throws SQLException, ClassNotFoundException {
        connection = DatabaseConnection.getConnection();
    }
    
    /**
     * Add a new product to the database
     * 
     * @param product The product to add
     * @return Number of rows affected (1 if successful, 0 if failed)`
     * @throws SQLException If a database error occurs
     */
    public int addProduct(Products product) throws SQLException {
        String query = "INSERT INTO species (Species_name, Category_id, Species_price, Species_photo, Species_quantity, Species_description) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, product.getName());
            statement.setInt(2, product.getCategoryId());
            statement.setBigDecimal(3, product.getPrice());
            statement.setString(4, product.getPhoto());
            statement.setInt(5, product.getQuantity());
            statement.setString(6, product.getDescription()); // Add description parameter
            
            return statement.executeUpdate();
        }
    }
    
    /**
     * Update an existing product
     * 
     * @param product The product to update
     * @return Number of rows affected (1 if successful, 0 if failed)
     * @throws SQLException If a database error occurs
     */
    public int updateProduct(Products product) throws SQLException {
        String query = "UPDATE species SET Species_name = ?, Category_id = ?, Species_price = ?, Species_photo = ?, Species_quantity = ?, Species_description = ? WHERE Species_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, product.getName());
            statement.setInt(2, product.getCategoryId());
            statement.setBigDecimal(3, product.getPrice());
            statement.setString(4, product.getPhoto());
            statement.setInt(5, product.getQuantity());
            statement.setString(6, product.getDescription()); // Add description parameter
            statement.setInt(7, product.getId());
            
            return statement.executeUpdate();
        }
    }
    
    /**
     * Update a product's quantity
     * 
     * @param productId The product ID
     * @param quantity The new quantity
     * @return Number of rows affected (1 if successful, 0 if failed)
     * @throws SQLException If a database error occurs
     */
    public int updateProductQuantity(int productId, int quantity) throws SQLException {
        String query = "UPDATE species SET Species_quantity = ? WHERE Species_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, quantity);
            statement.setInt(2, productId);
            
            return statement.executeUpdate();
        }
    }
    
    /**
     * Delete a product by ID
     * 
     * @param productId The ID of the product to delete
     * @return Number of rows affected (1 if successful, 0 if failed)
     * @throws SQLException If a database error occurs
     */
    public int deleteProduct(int productId) throws SQLException {
        String query = "DELETE FROM species WHERE Species_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, productId);
            
            return statement.executeUpdate();
        }
    }
    
    /**
     * Get a product by ID
     * 
     * @param productId The ID of the product to retrieve
     * @return The product object or null if not found
     * @throws SQLException If a database error occurs
     */
    public Products getProductById(int productId) throws SQLException {
        String query = "SELECT s.*, c.Category_name FROM species s " +
                       "JOIN category c ON s.Category_id = c.Category_id " +
                       "WHERE s.Species_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, productId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Products product = new Products();
                    product.setId(resultSet.getInt("Species_id"));
                    product.setName(resultSet.getString("Species_name"));
                    product.setCategoryId(resultSet.getInt("Category_id"));
                    product.setPrice(resultSet.getBigDecimal("Species_price"));
                    product.setPhoto(resultSet.getString("Species_photo"));
                    product.setCategoryName(resultSet.getString("Category_name"));
                    product.setQuantity(resultSet.getInt("Species_quantity"));
                    product.setDescription(resultSet.getString("Species_description")); // Get description
                    
                    return product;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Get all products from the database
     * 
     * @return List of all products
     * @throws SQLException If a database error occurs
     */
    public List<Products> getAllProducts() throws SQLException {
        List<Products> products = new ArrayList<>();
        
        String query = "SELECT s.*, c.Category_name FROM species s " +
                       "JOIN category c ON s.Category_id = c.Category_id " +
                       "ORDER BY s.Species_name";
        
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                Products product = new Products();
                product.setId(resultSet.getInt("Species_id"));
                product.setName(resultSet.getString("Species_name"));
                product.setCategoryId(resultSet.getInt("Category_id"));
                product.setPrice(resultSet.getBigDecimal("Species_price"));
                product.setPhoto(resultSet.getString("Species_photo"));
                product.setCategoryName(resultSet.getString("Category_name"));
                product.setQuantity(resultSet.getInt("Species_quantity"));
                product.setDescription(resultSet.getString("Species_description")); // Get description
                
                products.add(product);
            }
        }
        
        return products;
    }
    
    /**
     * Get products by category
     * 
     * @param categoryId The category ID
     * @return List of products in the category
     * @throws SQLException If a database error occurs
     */
    public List<Products> getProductsByCategory(int categoryId) throws SQLException {
        List<Products> products = new ArrayList<>();
        
        String query = "SELECT s.*, c.Category_name FROM species s " +
                      "JOIN category c ON s.Category_id = c.Category_id " +
                      "WHERE s.Category_id = ? " +
                      "ORDER BY s.Species_name";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, categoryId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Products product = new Products();
                    product.setId(resultSet.getInt("Species_id"));
                    product.setName(resultSet.getString("Species_name"));
                    product.setCategoryId(resultSet.getInt("Category_id"));
                    product.setPrice(resultSet.getBigDecimal("Species_price"));
                    product.setPhoto(resultSet.getString("Species_photo"));
                    product.setCategoryName(resultSet.getString("Category_name"));
                    product.setQuantity(resultSet.getInt("Species_quantity"));
                    product.setDescription(resultSet.getString("Species_description")); // Get description
                    
                    products.add(product);
                }
            }
        }
        
        return products;
    }
    
    /**
     * Check if a product with the given ID exists
     * 
     * @param productId The ID to check
     * @return true if exists, false otherwise
     * @throws SQLException If a database error occurs
     */
    public boolean productExists(int productId) throws SQLException {
        String query = "SELECT COUNT(*) FROM species WHERE Species_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, productId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Search for products by name or ID
     * 
     * @param searchTerm The search term to look for in product names or IDs
     * @return List of products matching the search term
     * @throws SQLException If a database error occurs
     */
    public List<Products> searchProductsByNameOrId(String searchTerm) throws SQLException {
        List<Products> products = new ArrayList<>();
        
        // First check if the search term is a number (possible ID)
        boolean isNumeric = searchTerm.matches("\\d+");
        
        // Base query
        String query = "SELECT s.*, c.Category_name FROM species s " +
                      "JOIN category c ON s.Category_id = c.Category_id " +
                      "WHERE ";
        
        // If it's a number, search by ID or name
        if (isNumeric) {
            query += "s.Species_id = ? OR s.Species_name LIKE ? ";
        } else {
            // If not a number, just search by name
            query += "s.Species_name LIKE ? ";
        }
        
        query += "ORDER BY s.Species_name";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            if (isNumeric) {
                // If it's a number, set both parameters
                int id = Integer.parseInt(searchTerm);
                statement.setInt(1, id);
                statement.setString(2, "%" + searchTerm + "%");
            } else {
                // If not a number, just set the name parameter
                statement.setString(1, "%" + searchTerm + "%");
            }
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Products product = new Products();
                    product.setId(resultSet.getInt("Species_id"));
                    product.setName(resultSet.getString("Species_name"));
                    product.setCategoryId(resultSet.getInt("Category_id"));
                    product.setPrice(resultSet.getBigDecimal("Species_price"));
                    product.setPhoto(resultSet.getString("Species_photo"));
                    product.setCategoryName(resultSet.getString("Category_name"));
                    product.setQuantity(resultSet.getInt("Species_quantity"));
                    product.setDescription(resultSet.getString("Species_description")); // Get description
                    
                    products.add(product);
                }
            }
        }
        
        return products;
    }
    
    /**
     * Get the ID of the last inserted product
     * 
     * @return The ID of the last inserted product or -1 if error occurs
     * @throws SQLException If a database error occurs
     */
    public int getLastInsertedProductId() throws SQLException {
        String query = "SELECT MAX(Species_id) as last_id FROM species";
        
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            
            if (resultSet.next()) {
                return resultSet.getInt("last_id");
            }
        }
        
        return -1; // Return -1 if no product is found
    }
    
    /**
     * Get products with low stock (less than 5 items)
     * 
     * @return List of products with low stock
     * @throws SQLException If a database error occurs
     */
    public List<Products> getLowStockProducts() throws SQLException {
        List<Products> products = new ArrayList<>();
        
        String query = "SELECT s.*, c.Category_name FROM species s " +
                       "JOIN category c ON s.Category_id = c.Category_id " +
                       "WHERE s.Species_quantity < 5 AND s.Species_quantity > 0 " +
                       "ORDER BY s.Species_quantity ASC";
        
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                Products product = new Products();
                product.setId(resultSet.getInt("Species_id"));
                product.setName(resultSet.getString("Species_name"));
                product.setCategoryId(resultSet.getInt("Category_id"));
                product.setPrice(resultSet.getBigDecimal("Species_price"));
                product.setPhoto(resultSet.getString("Species_photo"));
                product.setCategoryName(resultSet.getString("Category_name"));
                product.setQuantity(resultSet.getInt("Species_quantity"));
                product.setDescription(resultSet.getString("Species_description")); // Get description
                
                products.add(product);
            }
        }
        
        return products;
    }
    
    /**
     * Get out of stock products
     * 
     * @return List of out of stock products
     * @throws SQLException If a database error occurs
     */
    public List<Products> getOutOfStockProducts() throws SQLException {
        List<Products> products = new ArrayList<>();
        
        String query = "SELECT s.*, c.Category_name FROM species s " +
                       "JOIN category c ON s.Category_id = c.Category_id " +
                       "WHERE s.Species_quantity <= 0 " +
                       "ORDER BY s.Species_name";
        
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                Products product = new Products();
                product.setId(resultSet.getInt("Species_id"));
                product.setName(resultSet.getString("Species_name"));
                product.setCategoryId(resultSet.getInt("Category_id"));
                product.setPrice(resultSet.getBigDecimal("Species_price"));
                product.setPhoto(resultSet.getString("Species_photo"));
                product.setCategoryName(resultSet.getString("Category_name"));
                product.setQuantity(resultSet.getInt("Species_quantity"));
                product.setDescription(resultSet.getString("Species_description")); // Get description
                
                products.add(product);
            }
        }
        
        return products;
    }

    /**
     * Search for products matching the search term
     * 
     * @param searchTerm The search term to match against product name or description
     * @return List of products matching the search term
     * @throws SQLException if a database error occurs
     */
    public List<Products> searchProducts(String searchTerm) throws SQLException {
        List<Products> products = new ArrayList<>();
        String sql = "SELECT s.*, c.Category_name FROM species s " +
                     "JOIN category c ON s.Category_id = c.Category_id " +
                     "WHERE s.Species_name LIKE ? OR s.Species_description LIKE ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Set search parameters with wildcards for partial matching
            String searchPattern = "%" + searchTerm + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Products product = new Products();
                    product.setId(rs.getInt("Species_id"));
                    product.setName(rs.getString("Species_name"));
                    product.setDescription(rs.getString("Species_description"));
                    product.setPrice(rs.getBigDecimal("Species_price"));
                    product.setQuantity(rs.getInt("Species_quantity"));
                    product.setPhoto(rs.getString("Species_photo"));
                    product.setCategoryId(rs.getInt("Category_id"));
                    product.setCategoryName(rs.getString("Category_name"));
                    
                    products.add(product);
                }
            }
        }
        
        return products;
    }
    
    /**
     * Close the database connection
     */
    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

}