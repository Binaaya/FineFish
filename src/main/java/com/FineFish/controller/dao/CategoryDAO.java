package com.FineFish.controller.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.FineFish.controller.database.DatabaseConnection;
import com.FineFish.model.Category;

/**
 * Data Access Object for Category
 * Handles database operations related to categories
 */
public class CategoryDAO {
    
    private Connection connection;
    
    /**
     * Constructor - establishes database connection
     * 
     * @throws SQLException If a database error occurs
     * @throws ClassNotFoundException If the database driver is not found
     */
    public CategoryDAO() throws SQLException, ClassNotFoundException {
        connection = DatabaseConnection.getConnection();
    }
    
    /**
     * Add a new category to the database
     * 
     * @param category The category to add
     * @return Number of rows affected (1 if successful, 0 if failed)
     * @throws SQLException If a database error occurs
     */
    public int addCategory(Category category) throws SQLException {
        String query = "INSERT INTO Category (Category_id, Category_name) VALUES (?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, category.getId());
            statement.setString(2, category.getName());
            
            return statement.executeUpdate();
        }
    }
    
    
    /**
     * Get a category by ID
     * 
     * @param categoryId The ID of the category to retrieve
     * @return The category object or null if not found
     * @throws SQLException If a database error occurs
     */
    public Category getCategoryById(int categoryId) throws SQLException {
        String query = "SELECT c.*, COUNT(s.Species_id) as product_count FROM Category c " +
                      "LEFT JOIN species s ON c.Category_id = s.Category_id " +
                      "WHERE c.Category_id = ? " +
                      "GROUP BY c.Category_id";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, categoryId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Category category = new Category();
                    category.setId(resultSet.getInt("Category_id"));
                    category.setName(resultSet.getString("Category_name"));
                    category.setProductCount(resultSet.getInt("product_count"));
                    
                    return category;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Get all categories from the database with product counts
     * 
     * @return List of all categories with their product counts
     * @throws SQLException If a database error occurs
     */
    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT c.*, COUNT(s.Species_id) as product_count " +
                      "FROM Category c " +
                      "LEFT JOIN species s ON c.Category_id = s.Category_id " +
                      "GROUP BY c.Category_id, c.Category_name " +
                      "ORDER BY c.Category_name";
        
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                Category category = new Category();
                category.setId(resultSet.getInt("Category_id"));
                category.setName(resultSet.getString("Category_name"));
                category.setProductCount(resultSet.getInt("product_count"));
                
                categories.add(category);
            }
        }
        
        return categories;
    }
    
    /**
     * Check if a category with the given ID exists
     * 
     * @param categoryId The ID to check
     * @return true if exists, false otherwise
     * @throws SQLException If a database error occurs
     */
    public boolean categoryExists(int categoryId) throws SQLException {
        String query = "SELECT COUNT(*) FROM Category WHERE Category_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, categoryId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        
        return false;
    }
    
    
    /**
     * Count number of species in a category
     * 
     * @param categoryId The category ID
     * @return Number of species in the category
     * @throws SQLException If a database error occurs
     */
    public int countSpeciesInCategory(int categoryId) throws SQLException {
        String query = "SELECT COUNT(*) FROM species WHERE Category_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, categoryId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }
        
        return 0;
    }
    
    /**
     * Get total number of products across all categories
     * 
     * @return Total number of products
     * @throws SQLException If a database error occurs
     */
    public int getTotalProductCount() throws SQLException {
        String query = "SELECT COUNT(*) FROM species";

        try (Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }

        return 0;
    }
    /**
     * Close the database connection
     * 
     * @throws SQLException If a database error occurs
     */
    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}