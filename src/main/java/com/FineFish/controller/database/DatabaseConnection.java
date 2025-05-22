package com.FineFish.controller.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for database connection management
 */
public class DatabaseConnection {
    
    // JDBC driver name and database URL
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/finefish";
    
    // Database credentials
    private static final String USER = "root";
    private static final String PASS = "";
    
    private static Connection connection = null;
    
    /**
     * Get a connection to the database
     * @return Connection object
     * @throws ClassNotFoundException if the database driver is not found
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        try {
            // Register JDBC driver
            Class.forName(JDBC_DRIVER);
            
            // If connection doesn't exist or is closed, create a new one
            if (connection == null || connection.isClosed()) {
                // Open a connection
                connection = DriverManager.getConnection(DB_URL, USER, PASS);
            }
            
            return connection;
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found: " + e.getMessage());
            throw e;
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Close the database connection
     * @throws SQLException if a database access error occurs
     */
    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                System.out.println("Error closing database connection: " + e.getMessage());
                throw e;
            }
        }
    }
}