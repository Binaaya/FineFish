package com.FineFish.controller.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.FineFish.controller.database.DatabaseConnection;
import com.FineFish.model.Products;
import com.FineFish.model.Sale;
import com.FineFish.model.User;

/**
 * Servlet implementation class AdminDashboardServlet
 * Handles retrieving and displaying admin dashboard data
 */
@WebServlet("/AdminDashboardServlet")
public class AdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * Default constructor
     */
    public AdminDashboardServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is logged in and is admin
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null || !"admin".equals(user.getRole())) {
            // Not an admin, redirect to login
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/pages/user/Login.jsp");
            } else {
                session.setAttribute("errorMessage", "Access denied. Admin privileges required.");
                response.sendRedirect(request.getContextPath() + "/pages/user/Dashboard.jsp");
            }
            return;
        }
        
        Connection conn = null;
        
        try {
            // Get database connection
            conn = DatabaseConnection.getConnection();
            
            // Get dashboard statistics
            int totalProducts = getTotalProducts(conn);
            int totalOrders = getTotalOrders(conn);
            double totalRevenue = getTotalRevenue(conn);
            int activeUsers = getTotalUsers(conn);
            
            // Get recent orders
            List<Sale> recentOrders = getRecentOrders(conn, 5);
            
            // Get popular products with sales counts
            Map<Integer, Integer> productSalesMap = new HashMap<>();
            List<Products> popularProducts = getPopularProducts(conn, 4, productSalesMap);
            
            // Set attributes for the JSP page
            request.setAttribute("totalProducts", totalProducts);
            request.setAttribute("totalOrders", totalOrders);
            request.setAttribute("totalRevenue", totalRevenue);
            request.setAttribute("activeUsers", activeUsers);
            request.setAttribute("recentOrders", recentOrders);
            request.setAttribute("popularProducts", popularProducts);
            request.setAttribute("productSalesMap", productSalesMap);
            
            // Forward to the dashboard page
            request.getRequestDispatcher("/pages/admin/Dashboard.jsp").forward(request, response);
            
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error retrieving dashboard data: " + e.getMessage());
            e.printStackTrace();
            
            request.setAttribute("errorMessage", "A system error occurred. Please try again later.");
            request.getRequestDispatcher("/pages/admin/Dashboard.jsp").forward(request, response);
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Post requests handled same as get
        doGet(request, response);
    }
    
    /**
     * Get total number of products
     * 
     * @param conn Database connection
     * @return Total number of products
     * @throws SQLException If a database error occurs
     */
    private int getTotalProducts(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM species";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
            
            return 0;
        }
    }
    
    /**
     * Get total number of orders
     * 
     * @param conn Database connection
     * @return Total number of orders
     * @throws SQLException If a database error occurs
     */
    private int getTotalOrders(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM `order`";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
            
            return 0;
        }
    }
    
    /**
     * Get total revenue
     * 
     * @param conn Database connection
     * @return Total revenue
     * @throws SQLException If a database error occurs
     */
    private double getTotalRevenue(Connection conn) throws SQLException {
        String sql = "SELECT SUM(Total_amount) AS total FROM `order`";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
            
            return 0.0;
        }
    }
    
    /**
     * Get total number of users
     * 
     * @param conn Database connection
     * @return Total number of users
     * @throws SQLException If a database error occurs
     */
    private int getTotalUsers(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM `user`";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
            
            return 0;
        }
    }
    
    /**
     * Get recent orders
     * 
     * @param conn Database connection
     * @param limit Number of orders to retrieve
     * @return List of recent orders
     * @throws SQLException If a database error occurs
     */
    private List<Sale> getRecentOrders(Connection conn, int limit) throws SQLException {
        List<Sale> orders = new ArrayList<>();
        
        String sql = "SELECT o.Order_id, o.User_id, u.Username, o.Order_date, o.Total_amount " +
                    "FROM `order` o " +
                    "LEFT JOIN `user` u ON o.User_id = u.User_id " +
                    "ORDER BY o.Order_date DESC " +
                    "LIMIT ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Sale order = new Sale();
                    order.setOrderId(rs.getInt("Order_id"));
                    order.setUserId(rs.getInt("User_id"));
                    order.setUsername(rs.getString("Username"));
                    order.setOrderDate(rs.getDate("Order_date"));
                    order.setAmount(rs.getDouble("Total_amount"));
                    
                    orders.add(order);
                }
            }
        }
        
        return orders;
    }
    
    /**
     * Get popular products by quantity sold
     * 
     * @param conn Database connection
     * @param limit Number of products to retrieve
     * @param productSalesMap Map to store product sales counts
     * @return List of popular products
     * @throws SQLException If a database error occurs
     */
    private List<Products> getPopularProducts(Connection conn, int limit, Map<Integer, Integer> productSalesMap) throws SQLException {
        List<Products> products = new ArrayList<>();
        
        // Query to get top selling products based on order_species table
        String sql = "SELECT s.Species_id, s.Species_name, s.Category_id, s.Species_price, s.Species_photo, " +
                    "c.Category_name, COUNT(os.Species_id) AS sales_count " +
                    "FROM species s " +
                    "LEFT JOIN category c ON s.Category_id = c.Category_id " +
                    "LEFT JOIN order_species os ON s.Species_id = os.Species_id " +
                    "GROUP BY s.Species_id " +
                    "ORDER BY sales_count DESC " +
                    "LIMIT ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Products product = new Products();
                    product.setId(rs.getInt("Species_id"));
                    product.setName(rs.getString("Species_name"));
                    product.setCategoryId(rs.getInt("Category_id"));
                    product.setPrice(rs.getBigDecimal("Species_price"));
                    product.setPhoto(rs.getString("Species_photo"));
                    product.setCategoryName(rs.getString("Category_name"));
                    
                    // Store sales count in the map
                    productSalesMap.put(product.getId(), rs.getInt("sales_count"));
                    
                    products.add(product);
                }
            }
        }
        
        // If no products with sales, get some products anyway
        if (products.isEmpty()) {
            sql = "SELECT s.Species_id, s.Species_name, s.Category_id, s.Species_price, s.Species_photo, " +
                  "c.Category_name " +
                  "FROM species s " +
                  "LEFT JOIN category c ON s.Category_id = c.Category_id " +
                  "ORDER BY s.Species_id " +
                  "LIMIT ?";
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, limit);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Products product = new Products();
                        product.setId(rs.getInt("Species_id"));
                        product.setName(rs.getString("Species_name"));
                        product.setCategoryId(rs.getInt("Category_id"));
                        product.setPrice(rs.getBigDecimal("Species_price"));
                        product.setPhoto(rs.getString("Species_photo"));
                        product.setCategoryName(rs.getString("Category_name"));
                        
                        // Set default sales count to 0
                        productSalesMap.put(product.getId(), 0);
                        
                        products.add(product);
                    }
                }
            }
        }
        
        return products;
    }
}