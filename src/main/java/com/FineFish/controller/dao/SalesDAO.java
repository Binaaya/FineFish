package com.FineFish.controller.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.FineFish.controller.database.DatabaseConnection;
import com.FineFish.model.Sale;

/**
 * Data Access Object for sales-related operations
 */
public class SalesDAO {
    
    /**
     * Retrieves sales report data based on date filters
     * 
     * @param startDate Optional start date filter (null for no start date filter)
     * @param endDate Optional end date filter (null for no end date filter)
     * @return List of Sale objects containing sales data
     * @throws SQLException If a database error occurs
     * @throws ClassNotFoundException If the database driver is not found
     */
    public List<Sale> getSalesReport(Date startDate, Date endDate) throws SQLException, ClassNotFoundException {
        List<Sale> salesList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            // Get database connection
            conn = DatabaseConnection.getConnection();
            
            // Build SQL query based on filters
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT o.Order_id, o.User_id, u.Username, o.Order_date, o.Total_amount, ");
            sql.append("(SELECT COUNT(*) FROM order_species os WHERE os.Order_id = o.Order_id) AS item_count ");
            sql.append("FROM `order` o ");
            sql.append("LEFT JOIN `user` u ON o.User_id = u.User_id ");
            
            // Add date filters if provided
            if (startDate != null || endDate != null) {
                sql.append("WHERE ");
                
                if (startDate != null) {
                    sql.append("o.Order_date >= ? ");
                    
                    if (endDate != null) {
                        sql.append("AND ");
                    }
                }
                
                if (endDate != null) {
                    sql.append("o.Order_date <= ? ");
                }
            }
            
            // Add order by clause
            sql.append("ORDER BY o.Order_date DESC");
            
            // Prepare statement
            stmt = conn.prepareStatement(sql.toString());
            
            // Set date parameters if provided
            int paramIndex = 1;
            
            if (startDate != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = sdf.format(startDate);
                stmt.setString(paramIndex++, formattedDate);
            }
            
            if (endDate != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = sdf.format(endDate);
                stmt.setString(paramIndex++, formattedDate);
            }
            
            // Execute query
            rs = stmt.executeQuery();
            
            // Process results
            while (rs.next()) {
                Sale sale = new Sale();
                sale.setOrderId(rs.getInt("Order_id"));
                sale.setUserId(rs.getInt("User_id"));
                sale.setUsername(rs.getString("Username"));
                sale.setOrderDate(rs.getDate("Order_date"));
                sale.setAmount(rs.getDouble("Total_amount"));
                sale.setItemCount(rs.getInt("item_count"));
                
                salesList.add(sale);
            }
            
            return salesList;
            
        } finally {
            // Close resources
            if (rs != null) {
                rs.close();
            }
            
            if (stmt != null) {
                stmt.close();
            }
            
            // Connection is managed by DatabaseConnection class
        }
    }
    
    /**
     * Gets total sales amount for a specified period
     * 
     * @param startDate Optional start date (null for no start date)
     * @param endDate Optional end date (null for no end date)
     * @return Total sales amount
     * @throws SQLException If a database error occurs
     * @throws ClassNotFoundException If the database driver is not found
     */
    public double getTotalSales(Date startDate, Date endDate) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        double totalSales = 0.0;
        
        try {
            // Get database connection
            conn = DatabaseConnection.getConnection();
            
            // Build SQL query based on filters
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT SUM(Total_amount) AS total_sales ");
            sql.append("FROM `order` ");
            
            // Add date filters if provided
            if (startDate != null || endDate != null) {
                sql.append("WHERE ");
                
                if (startDate != null) {
                    sql.append("Order_date >= ? ");
                    
                    if (endDate != null) {
                        sql.append("AND ");
                    }
                }
                
                if (endDate != null) {
                    sql.append("Order_date <= ? ");
                }
            }
            
            // Prepare statement
            stmt = conn.prepareStatement(sql.toString());
            
            // Set date parameters if provided
            int paramIndex = 1;
            
            if (startDate != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = sdf.format(startDate);
                stmt.setString(paramIndex++, formattedDate);
            }
            
            if (endDate != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = sdf.format(endDate);
                stmt.setString(paramIndex++, formattedDate);
            }
            
            // Execute query
            rs = stmt.executeQuery();
            
            // Process result
            if (rs.next()) {
                totalSales = rs.getDouble("total_sales");
            }
            
            return totalSales;
            
        } finally {
            // Close resources
            if (rs != null) {
                rs.close();
            }
            
            if (stmt != null) {
                stmt.close();
            }
            
            // Connection is managed by DatabaseConnection class
        }
    }
}