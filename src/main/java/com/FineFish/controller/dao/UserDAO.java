package com.FineFish.controller.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.FineFish.controller.database.DatabaseConnection;
import com.FineFish.model.User;
import com.FineFish.utility.EncryptDecrypt;

/**
 * Data Access Object for User-related database operations
 */
public class UserDAO {
    
    private Connection connection;
    
    /**
     * Constructor - initializes the database connection
     * @throws ClassNotFoundException if the JDBC driver is not found
     * @throws SQLException if a database access error occurs
     */
    public UserDAO() throws ClassNotFoundException, SQLException {
        connection = DatabaseConnection.getConnection();
    }
    
    /**
     * Authenticates a user based on username/email and password
     * @param usernameOrEmail the username or email to check
     * @param password the password to verify
     * @return User object if authentication successful, null otherwise
     * @throws SQLException if a database access error occurs
     */
    public User authenticateUser(String usernameOrEmail, String password) throws SQLException {
        String sql = "SELECT * FROM `user` WHERE (Username = ? OR Email = ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, usernameOrEmail);
            statement.setString(2, usernameOrEmail);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Get stored password from database
                    String storedPassword = resultSet.getString("Password");
                    
                    // Check password using the checkPassword method
                    if (EncryptDecrypt.checkPassword(password, storedPassword)) {
                        // Create and return user object
                        User user = new User();
                        user.setUserId(resultSet.getInt("User_id"));
                        user.setUsername(resultSet.getString("Username"));
                        user.setFname(resultSet.getString("Fname"));
                        user.setSurname(resultSet.getString("Surname"));
                        user.setEmail(resultSet.getString("Email"));
                        user.setPassword(storedPassword); // Store encrypted password
                        user.setAddress(resultSet.getString("Address"));
                        user.setPhoneNumber(resultSet.getString("Phone_number"));
                        user.setRole(resultSet.getString("Role"));
                        return user;
                    }
                }
            }
        }
        
        return null; // Authentication failed
    }
    
    /**
     * Registers a new user in the database
     * @param user the User object to be registered
     * @return true if registration was successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean registerUser(User user) throws SQLException {
        String sql = "INSERT INTO `user` (Username, Fname, Surname, Email, Password, Address, Phone_number, Role) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getFname());
            statement.setString(3, user.getSurname());
            statement.setString(4, user.getEmail());
            
            // Encrypt the password before storing
            statement.setString(5, EncryptDecrypt.encrypt(user.getPassword()));
            
            statement.setString(6, user.getAddress());
            statement.setString(7, user.getPhoneNumber());
            statement.setString(8, user.getRole());
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    /**
     * Checks if a username already exists in the database
     * @param username the username to check
     * @return true if the username exists, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean isUsernameExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM `user` WHERE Username = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Checks if an email already exists in the database
     * @param email the email to check
     * @return true if the email exists, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean isEmailExists(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM `user` WHERE Email = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Updates a user's information in the database
     * @param user the User object with updated information
     * @return true if update was successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean updateUser(User user) throws SQLException {
        String sql = "UPDATE `user` SET Username = ?, Fname = ?, Surname = ?, Email = ?, " +
                     "Address = ?, Phone_number = ?, Role = ? WHERE User_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getFname());
            statement.setString(3, user.getSurname());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getAddress());
            statement.setString(6, user.getPhoneNumber());
            statement.setString(7, user.getRole());
            statement.setInt(8, user.getUserId());
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    /**
     * Updates a user's password in the database
     * @param userId the ID of the user
     * @param newPassword the new password
     * @return true if password update was successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean updatePassword(int userId, String newPassword) throws SQLException {
        String sql = "UPDATE `user` SET Password = ? WHERE User_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Encrypt the new password before storing
            statement.setString(1, EncryptDecrypt.encrypt(newPassword));
            statement.setInt(2, userId);
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    /**
     * Retrieves a user by their ID
     * @param userId the ID of the user to retrieve
     * @return User object if found, null otherwise
     * @throws SQLException if a database access error occurs
     */
    public User getUserById(int userId) throws SQLException {
        String sql = "SELECT * FROM `user` WHERE User_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setUserId(resultSet.getInt("User_id"));
                    user.setUsername(resultSet.getString("Username"));
                    user.setFname(resultSet.getString("Fname"));
                    user.setSurname(resultSet.getString("Surname"));
                    user.setEmail(resultSet.getString("Email"));
                    user.setPassword(resultSet.getString("Password"));  // Include password
                    user.setAddress(resultSet.getString("Address"));
                    user.setPhoneNumber(resultSet.getString("Phone_number"));
                    user.setRole(resultSet.getString("Role"));
                    return user;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Retrieves all users from the database
     * @return List of User objects
     * @throws SQLException if a database access error occurs
     */
    public List<User> getAllUsers() throws SQLException {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM `user`";
        
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                User user = new User();
                user.setUserId(resultSet.getInt("User_id"));
                user.setUsername(resultSet.getString("Username"));
                user.setFname(resultSet.getString("Fname"));
                user.setSurname(resultSet.getString("Surname"));
                user.setEmail(resultSet.getString("Email"));
                // Do not include password for security reasons
                user.setAddress(resultSet.getString("Address"));
                user.setPhoneNumber(resultSet.getString("Phone_number"));
                user.setRole(resultSet.getString("Role"));
                userList.add(user);
            }
        }
        
        return userList;
    }
    
    /**
     * Deletes a user and all related data from the database
     * This method handles cascade deletion of all related records
     * @param userId the ID of the user to delete
     * @return true if deletion was successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean deleteUser(int userId) throws SQLException {
        boolean success = false;
        
        // Start transaction
        try {
            connection.setAutoCommit(false);
            
            // 1. Delete from cart_species table
            String deleteCartSpecies = "DELETE FROM cart_species WHERE Cart_id IN (SELECT Cart_id FROM cart WHERE User_id = ?)";
            try (PreparedStatement statement = connection.prepareStatement(deleteCartSpecies)) {
                statement.setInt(1, userId);
                statement.executeUpdate();
            }
            
            // 2. Delete from cart table
            String deleteCart = "DELETE FROM cart WHERE User_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(deleteCart)) {
                statement.setInt(1, userId);
                statement.executeUpdate();
            }
            
            // 3. Delete from order_species table
            String deleteOrderSpecies = "DELETE FROM order_species WHERE Order_id IN (SELECT Order_id FROM `order` WHERE User_id = ?)";
            try (PreparedStatement statement = connection.prepareStatement(deleteOrderSpecies)) {
                statement.setInt(1, userId);
                statement.executeUpdate();
            }
            
            // 4. Delete from order table
            String deleteOrders = "DELETE FROM `order` WHERE User_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(deleteOrders)) {
                statement.setInt(1, userId);
                statement.executeUpdate();
            }
            
            // 5. Finally, delete the user
            String deleteUser = "DELETE FROM `user` WHERE User_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(deleteUser)) {
                statement.setInt(1, userId);
                int rowsAffected = statement.executeUpdate();
                success = rowsAffected > 0;
            }
            
            // Commit transaction
            connection.commit();
            
        } catch (SQLException e) {
            // Rollback transaction on error
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Error during rollback: " + rollbackEx.getMessage());
            }
            throw e;
        } finally {
            // Restore auto-commit
            try {
                connection.setAutoCommit(true);
            } catch (SQLException autoCommitEx) {
                System.err.println("Error setting auto-commit: " + autoCommitEx.getMessage());
            }
        }
        
        return success;
    }
    
    /**
     * Closes the database connection
     * @throws SQLException if a database access error occurs
     */
    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}