package com.FineFish.controller.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.FineFish.controller.database.DatabaseConnection;
import com.FineFish.model.CartItem;
import com.FineFish.model.Order;
import com.FineFish.model.OrderItem;

/**
 * Data Access Object for Order-related database operations
 */
public class OrderDAO {
    
    private Connection connection;
    
    /**
     * Constructor - initializes the database connection
     * @throws ClassNotFoundException if the JDBC driver is not found
     * @throws SQLException if a database access error occurs
     */
    public OrderDAO() throws ClassNotFoundException, SQLException {
        connection = DatabaseConnection.getConnection();
    }
    
    /**
     * Create a new order from a cart
     * 
     * @param userId The user ID
     * @return The order ID if successful, -1 otherwise
     * @throws SQLException If a database error occurs
     */
    public int createOrderFromCart(int userId) throws SQLException {
        CartDAO cartDAO = null;
        ProductDAO productDAO = null;
        
        try {
            cartDAO = new CartDAO();
            productDAO = new ProductDAO();
            List<CartItem> cartItems = cartDAO.getCartItems(userId);
            
            if (cartItems.isEmpty()) {
                return -1; // Cart is empty
            }
            
            // Begin transaction
            connection.setAutoCommit(false);
            
            // Insert order
            String orderQuery = "INSERT INTO `order` (User_id, Order_date, Total_amount) VALUES (?, NOW(), ?)";
            int orderId;
            
            // Calculate total amount
            double totalAmount = 0.0;
            for (CartItem item : cartItems) {
                totalAmount += item.getSubtotal().doubleValue();
            }
            
            try (PreparedStatement orderStmt = connection.prepareStatement(orderQuery, Statement.RETURN_GENERATED_KEYS)) {
                orderStmt.setInt(1, userId);
                orderStmt.setDouble(2, totalAmount);
                orderStmt.executeUpdate();
                
                try (ResultSet rs = orderStmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        orderId = rs.getInt(1);
                    } else {
                        connection.rollback();
                        return -1;
                    }
                }
            }
            
            // Insert order items and update product quantities
            String orderItemQuery = "INSERT INTO order_species (Order_id, Species_id, Price, Quantity) VALUES (?, ?, ?, ?)";
            String updateProductQuery = "UPDATE species SET Species_quantity = Species_quantity - ? WHERE Species_id = ?";
            
            try (PreparedStatement orderItemStmt = connection.prepareStatement(orderItemQuery);
                 PreparedStatement updateProductStmt = connection.prepareStatement(updateProductQuery)) {
                
                for (CartItem item : cartItems) {
                    // Insert order item
                    orderItemStmt.setInt(1, orderId);
                    orderItemStmt.setInt(2, item.getProductId());
                    orderItemStmt.setDouble(3, item.getPrice().doubleValue());
                    orderItemStmt.setInt(4, item.getQuantity());
                    orderItemStmt.addBatch();
                    
                    // Update product quantity
                    updateProductStmt.setInt(1, item.getQuantity());
                    updateProductStmt.setInt(2, item.getProductId());
                    updateProductStmt.addBatch();
                }
                
                orderItemStmt.executeBatch();
                updateProductStmt.executeBatch();
            }
            
            // Clear the cart
            cartDAO.clearCart(userId);
            
            // Commit transaction
            connection.commit();
            
            return orderId;
            
        } catch (Exception e) {
            // Rollback transaction on error
            try {
                connection.rollback();
            } catch (SQLException ex) {
            }
            throw new SQLException("Error creating order: " + e.getMessage(), e);
        } finally {
            // Reset auto-commit
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
            }
            
            // Close connections
            if (cartDAO != null) {
                try {
                    cartDAO.closeConnection();
                } catch (SQLException e) {
                }
            }
            
            if (productDAO != null) {
                try {
                    productDAO.closeConnection();
                } catch (SQLException e) {
                }
            }
        }
    }
    
    /**
     * Get an order by ID
     * 
     * @param orderId The order ID
     * @return The order if found, null otherwise
     * @throws SQLException If a database error occurs
     */
    public Order getOrderById(int orderId) throws SQLException {
        String query = "SELECT o.*, u.Username FROM `order` o " +
                      "JOIN `user` u ON o.User_id = u.User_id " +
                      "WHERE o.Order_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Order order = new Order();
                    order.setOrderId(rs.getInt("Order_id"));
                    order.setUserId(rs.getInt("User_id"));
                    order.setUsername(rs.getString("Username"));
                    order.setOrderDate(rs.getTimestamp("Order_date"));
                    order.setTotalAmount(rs.getDouble("Total_amount"));
                    
                    // Get order items
                    List<OrderItem> orderItems = getOrderItems(orderId);
                    order.setOrderItems(orderItems);
                    
                    return order;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Get order items for a specific order
     * 
     * @param orderId The order ID
     * @return List of order items
     * @throws SQLException If a database error occurs
     */
    private List<OrderItem> getOrderItems(int orderId) throws SQLException {
        List<OrderItem> orderItems = new ArrayList<>();
        String query = "SELECT os.*, s.Species_name, s.Species_photo, c.Category_name " +
                      "FROM order_species os " +
                      "JOIN species s ON os.Species_id = s.Species_id " +
                      "JOIN category c ON s.Category_id = c.Category_id " +
                      "WHERE os.Order_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem();
                    item.setOrderItemId(rs.getInt("Order_species_id"));
                    item.setOrderId(rs.getInt("Order_id"));
                    item.setProductId(rs.getInt("Species_id"));
                    item.setProductName(rs.getString("Species_name"));
                    item.setPrice(rs.getDouble("Price"));
                    item.setQuantity(rs.getInt("Quantity"));
                    item.setSubtotal(rs.getDouble("Price") * rs.getInt("Quantity"));
                    item.setImageUrl("images/products/" + rs.getString("Species_photo"));
                    item.setCategory(rs.getString("Category_name"));
                    
                    orderItems.add(item);
                }
            }
        }
        
        return orderItems;
    }
    
    /**
     * Get all orders for a specific user
     * 
     * @param userId The user ID
     * @return List of orders
     * @throws SQLException If a database error occurs
     */
    public List<Order> getOrdersByUserId(int userId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT o.*, u.Username FROM `order` o " +
                      "JOIN `user` u ON o.User_id = u.User_id " +
                      "WHERE o.User_id = ? " +
                      "ORDER BY o.Order_date DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setOrderId(rs.getInt("Order_id"));
                    order.setUserId(rs.getInt("User_id"));
                    order.setUsername(rs.getString("Username"));
                    order.setOrderDate(rs.getTimestamp("Order_date"));
                    order.setTotalAmount(rs.getDouble("Total_amount"));
                    
                    orders.add(order);
                }
            }
        }
        
        return orders;
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