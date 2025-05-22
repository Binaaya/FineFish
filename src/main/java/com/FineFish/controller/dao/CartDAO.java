package com.FineFish.controller.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.FineFish.controller.database.DatabaseConnection;
import com.FineFish.model.CartItem;
import com.FineFish.model.Products;

/**
 * Data Access Object for Cart-related database operations
 */
public class CartDAO {
    
    private Connection connection;
    
    /**
     * Constructor - initializes the database connection
     * @throws ClassNotFoundException if the JDBC driver is not found
     * @throws SQLException if a database access error occurs
     */
    public CartDAO() throws ClassNotFoundException, SQLException {
        connection = DatabaseConnection.getConnection();
    }
    
    /**
     * Get cart items for a specific user
     * 
     * @param userId The user ID
     * @return List of cart items
     * @throws SQLException If a database error occurs
     */
    public List<CartItem> getCartItems(int userId) throws SQLException {
        List<CartItem> cartItems = new ArrayList<>();
        String query = "SELECT cs.Cart_species_id, cs.Species_id, cs.quantity, " +
                       "s.Species_name, s.Species_price, s.Species_photo, s.Species_quantity, " +
                       "c.Category_name, s.Species_description " +
                       "FROM cart_species cs " +
                       "JOIN cart cart ON cs.Cart_id = cart.Cart_id " +
                       "JOIN species s ON cs.Species_id = s.Species_id " +
                       "JOIN category c ON s.Category_id = c.Category_id " +
                       "WHERE cart.User_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CartItem item = new CartItem();
                    item.setCartItemId(rs.getInt("Cart_species_id"));
                    item.setProductId(rs.getInt("Species_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setProductName(rs.getString("Species_name"));
                    item.setPrice(rs.getBigDecimal("Species_price"));
                    item.setImageUrl("images/products/" + rs.getString("Species_photo"));
                    item.setAvailableQuantity(rs.getInt("Species_quantity"));
                    item.setProductDescription(rs.getString("Category_name"));
                    
                    // Set the product description if available
                    if (rs.getString("Species_description") != null) {
                        item.setProductDescription(rs.getString("Species_description"));
                    }
                    
                    // Calculate subtotal
                    item.calculateSubtotal();
                    
                    cartItems.add(item);
                }
            }
        }
        
        return cartItems;
    }
    
    /**
     * Get the total number of unique items in a user's cart
     * 
     * @param userId The user ID
     * @return Total number of unique items
     * @throws SQLException If a database error occurs
     */
    public int getUniqueCartItemCount(int userId) throws SQLException {
        String query = "SELECT COUNT(*) AS unique_count FROM cart_species cs " +
                       "JOIN cart c ON cs.Cart_id = c.Cart_id " +
                       "WHERE c.User_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("unique_count");
                }
            }
        }
        
        return 0;
    }
    
    /**
     * Get or create a cart for a user
     * 
     * @param userId The user ID
     * @return Cart ID
     * @throws SQLException If a database error occurs
     */
    public int getOrCreateCart(int userId) throws SQLException {
        // First check if user already has a cart
        String checkQuery = "SELECT Cart_id FROM cart WHERE User_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(checkQuery)) {
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Cart_id");
                }
            }
        }
        
        // If not, create a new cart
        String insertQuery = "INSERT INTO cart (User_id, Cart_quantity) VALUES (?, 0)";
        
        try (PreparedStatement stmt = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Failed to create cart, no ID obtained.");
                }
            }
        }
    }
    
    /**
     * Add a product to the cart
     * 
     * @param userId The user ID
     * @param productId The product ID
     * @param quantity The quantity to add
     * @return true if successful, false otherwise
     * @throws SQLException If a database error occurs
     */
    public boolean addToCart(int userId, int productId, int quantity) throws SQLException {
        // First check available quantity
        ProductDAO productDAO = null;
        try {
            productDAO = new ProductDAO();
            Products product = productDAO.getProductById(productId);
            
            if (product == null || product.getQuantity() < quantity) {
                return false; // Not enough quantity available
            }
            
            // Get or create cart ID
            int cartId = getOrCreateCart(userId);
            
            // Check if the product is already in the cart
            String checkQuery = "SELECT quantity FROM cart_species WHERE Cart_id = ? AND Species_id = ?";
            
            try (PreparedStatement stmt = connection.prepareStatement(checkQuery)) {
                stmt.setInt(1, cartId);
                stmt.setInt(2, productId);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        // Product already exists in cart, update quantity
                        int currentQuantity = rs.getInt("quantity");
                        int newQuantity = currentQuantity + quantity;
                        
                        // Ensure we don't exceed available quantity
                        if (newQuantity > product.getQuantity()) {
                            newQuantity = product.getQuantity(); // Limit to available quantity
                        }
                        
                        String updateQuery = "UPDATE cart_species SET quantity = ? WHERE Cart_id = ? AND Species_id = ?";
                        
                        try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                            updateStmt.setInt(1, newQuantity);
                            updateStmt.setInt(2, cartId);
                            updateStmt.setInt(3, productId);
                            
                            int result = updateStmt.executeUpdate();
                            updateCartQuantity(cartId);
                            return result > 0;
                        }
                    } else {
                        // New product in cart
                        String insertQuery = "INSERT INTO cart_species (Cart_id, Species_id, quantity) VALUES (?, ?, ?)";
                        
                        try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                            insertStmt.setInt(1, cartId);
                            insertStmt.setInt(2, productId);
                            insertStmt.setInt(3, quantity);
                            
                            int result = insertStmt.executeUpdate();
                            updateCartQuantity(cartId);
                            return result > 0;
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database connection error", e);
        } finally {
            if (productDAO != null) {
                productDAO.closeConnection();
            }
        }
    }
    
    /**
     * Update cart quantity total
     * 
     * @param cartId The cart ID
     * @throws SQLException If a database error occurs
     */
    private void updateCartQuantity(int cartId) throws SQLException {
        String query = "UPDATE cart SET Cart_quantity = (SELECT SUM(quantity) FROM cart_species WHERE Cart_id = ?) WHERE Cart_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, cartId);
            stmt.setInt(2, cartId);
            stmt.executeUpdate();
        }
    }
    
    /**
     * Update a cart item quantity
     * 
     * @param cartItemId The cart item ID
     * @param quantity The new quantity
     * @return true if successful, false otherwise
     * @throws SQLException If a database error occurs
     */
    public boolean updateCartItemQuantity(int cartItemId, int quantity) throws SQLException {
        // First get the cart ID and product ID
        String getQuery = "SELECT cs.Cart_id, cs.Species_id, s.Species_quantity " +
                         "FROM cart_species cs " +
                         "JOIN species s ON cs.Species_id = s.Species_id " +
                         "WHERE cs.Cart_species_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(getQuery)) {
            stmt.setInt(1, cartItemId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int cartId = rs.getInt("Cart_id");
                    int availableQuantity = rs.getInt("Species_quantity");
                    
                    // Check if quantity is valid
                    if (quantity <= 0) {
                        return removeCartItem(cartItemId); // Remove item if quantity is zero or negative
                    }
                    
                    if (quantity > availableQuantity) {
                        quantity = availableQuantity; // Limit to available quantity
                    }
                    
                    // Update the quantity
                    String updateQuery = "UPDATE cart_species SET quantity = ? WHERE Cart_species_id = ?";
                    
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                        updateStmt.setInt(1, quantity);
                        updateStmt.setInt(2, cartItemId);
                        
                        int result = updateStmt.executeUpdate();
                        if (result > 0) {
                            updateCartQuantity(cartId);
                            return true;
                        }
                    }
                }
            }
        }
        
        return false;
    }
    
    /**
     * Remove an item from the cart
     * 
     * @param cartItemId The cart item ID
     * @return true if successful, false otherwise
     * @throws SQLException If a database error occurs
     */
    public boolean removeCartItem(int cartItemId) throws SQLException {
        // First get the cart ID before deletion
        int cartId = -1;
        String getCartIdQuery = "SELECT Cart_id FROM cart_species WHERE Cart_species_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(getCartIdQuery)) {
            stmt.setInt(1, cartItemId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cartId = rs.getInt("Cart_id");
                }
            }
        }
        
        if (cartId == -1) {
            return false; // Item not found
        }
        
        // Delete the cart item
        String deleteQuery = "DELETE FROM cart_species WHERE Cart_species_id = ?";
        try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
            deleteStmt.setInt(1, cartItemId);
            int result = deleteStmt.executeUpdate();
            
            if (result > 0) {
                updateCartQuantity(cartId);
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Clear all items from a user's cart
     * 
     * @param userId The user ID
     * @return true if successful, false otherwise
     * @throws SQLException If a database error occurs
     */
    public boolean clearCart(int userId) throws SQLException {
        // First get the cart ID
        String getQuery = "SELECT Cart_id FROM cart WHERE User_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(getQuery)) {
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int cartId = rs.getInt("Cart_id");
                    
                    // Delete all items from the cart
                    String deleteQuery = "DELETE FROM cart_species WHERE Cart_id = ?";
                    
                    try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
                        deleteStmt.setInt(1, cartId);
                        
                        deleteStmt.executeUpdate();
                        updateCartQuantity(cartId);
                        return true; // Return true regardless of how many rows were affected
                    }
                }
            }
        }
        
        return false;
    }
    
    /**
     * Get the total number of items in a user's cart
     * 
     * @param userId The user ID
     * @return Total number of items
     * @throws SQLException If a database error occurs
     */
    public int getCartItemCount(int userId) throws SQLException {
        String query = "SELECT Cart_quantity FROM cart WHERE User_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Cart_quantity");
                }
            }
        }
        
        return 0;
    }
    
    /**
     * Get the total amount for a user's cart
     * 
     * @param userId The user ID
     * @return Total amount
     * @throws SQLException If a database error occurs
     */
    public double getCartTotal(int userId) throws SQLException {
        String query = "SELECT SUM(cs.quantity * s.Species_price) AS total " +
                      "FROM cart_species cs " +
                      "JOIN species s ON cs.Species_id = s.Species_id " +
                      "JOIN cart c ON cs.Cart_id = c.Cart_id " +
                      "WHERE c.User_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        }
        
        return 0.0;
    }
    
    /**
     * Check if a product is already in the user's cart
     * 
     * @param userId The user ID
     * @param productId The product ID
     * @return true if the product is in the cart, false otherwise
     * @throws SQLException If a database error occurs
     */
    public boolean isProductInCart(int userId, int productId) throws SQLException {
        String query = "SELECT cs.Cart_species_id FROM cart_species cs " +
                      "JOIN cart c ON cs.Cart_id = c.Cart_id " +
                      "WHERE c.User_id = ? AND cs.Species_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Returns true if a record exists
            }
        }
    }
    
    /**
     * Get the current quantity of a product in the user's cart
     * 
     * @param userId The user ID
     * @param productId The product ID
     * @return The current quantity, or 0 if the product is not in the cart
     * @throws SQLException If a database error occurs
     */
    public int getProductQuantityInCart(int userId, int productId) throws SQLException {
        String query = "SELECT cs.quantity FROM cart_species cs " +
                      "JOIN cart c ON cs.Cart_id = c.Cart_id " +
                      "WHERE c.User_id = ? AND cs.Species_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("quantity");
                }
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