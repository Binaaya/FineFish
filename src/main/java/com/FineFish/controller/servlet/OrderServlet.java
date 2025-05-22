package com.FineFish.controller.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.FineFish.controller.dao.OrderDAO;
import com.FineFish.model.Order;
import com.FineFish.model.User;

/**
 * Servlet implementation class OrderServlet
 * Handles order operations (create, view details, view history)
 */
@WebServlet("/OrderServlet")
public class OrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * Default constructor
     */
    public OrderServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            // If not logged in, redirect to login page
            response.sendRedirect(request.getContextPath() + "/pages/user/Login.jsp");
            return;
        }
        
        // Get action parameter
        String action = request.getParameter("action");
        
        try {
            OrderDAO orderDAO = new OrderDAO();
            
            if ("view".equals(action)) {
                // View order details
                viewOrderDetails(request, response, orderDAO, user);
            } else if ("history".equals(action)) {
                // View order history
                viewOrderHistory(request, response, orderDAO, user);
            } else {
                // Default action - redirect to dashboard
                response.sendRedirect(request.getContextPath() + "/pages/user/Dashboard.jsp");
            }
            
            // Close connection
            orderDAO.closeConnection();
            
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "Error processing order: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/pages/user/Dashboard.jsp");
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            // If not logged in, redirect to login page
            response.sendRedirect(request.getContextPath() + "/pages/user/Login.jsp");
            return;
        }
        
        // Get action parameter
        String action = request.getParameter("action");
        
        try {
            OrderDAO orderDAO = new OrderDAO();
            
            if ("create".equals(action)) {
                // Create order from cart
                createOrder(request, response, orderDAO, user);
            } else {
                // Invalid action, redirect to dashboard
                response.sendRedirect(request.getContextPath() + "/pages/user/Dashboard.jsp");
            }
            
            // Close connection
            orderDAO.closeConnection();
            
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "Error processing order: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/pages/user/Dashboard.jsp");
        }
    }
    
    /**
     * View order details
     */
    private void viewOrderDetails(HttpServletRequest request, HttpServletResponse response, 
                                 OrderDAO orderDAO, User user) throws SQLException, IOException, ServletException {
        // Get order ID
        String orderIdStr = request.getParameter("orderId");
        
        if (orderIdStr == null || orderIdStr.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "Order ID is required");
            response.sendRedirect(request.getContextPath() + "/pages/user/Dashboard.jsp");
            return;
        }
        
        int orderId;
        
        try {
            orderId = Integer.parseInt(orderIdStr);
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Invalid order ID");
            response.sendRedirect(request.getContextPath() + "/pages/user/Dashboard.jsp");
            return;
        }
        
        // Get order details
        Order order = orderDAO.getOrderById(orderId);
        
        if (order == null) {
            request.getSession().setAttribute("errorMessage", "Order not found");
            response.sendRedirect(request.getContextPath() + "/pages/user/Dashboard.jsp");
            return;
        }
        
        // Security check - make sure the order belongs to the user (unless admin)
        if (order.getUserId() != user.getUserId() && !"admin".equals(user.getRole())) {
            request.getSession().setAttribute("errorMessage", "You don't have permission to view this order");
            response.sendRedirect(request.getContextPath() + "/pages/user/Dashboard.jsp");
            return;
        }
        
        // Set order in request attribute
        request.setAttribute("order", order);
        
        // Forward to order details page
        request.getRequestDispatcher("/pages/user/OrderDetails.jsp").forward(request, response);
    }
    
    /**
     * View order history
     */
    private void viewOrderHistory(HttpServletRequest request, HttpServletResponse response, 
                                 OrderDAO orderDAO, User user) throws SQLException, IOException, ServletException {
        // Get order history
        List<Order> orderHistory = orderDAO.getOrdersByUserId(user.getUserId());
        
        // Set order history in request attribute
        request.setAttribute("orders", orderHistory);
        
        // Forward to order history page
        request.getRequestDispatcher("/pages/user/Dashboard.jsp").forward(request, response);
    }
    
    /**
     * Create order from cart
     */
    private void createOrder(HttpServletRequest request, HttpServletResponse response, 
                            OrderDAO orderDAO, User user) throws SQLException, IOException {
        // Create order from cart
        int orderId = orderDAO.createOrderFromCart(user.getUserId());
        
        if (orderId > 0) {
            // Order created successfully
            // Clear cart count in session
            request.getSession().setAttribute("cartCount", 0);
            
            // Set success message
            request.getSession().setAttribute("successMessage", 
                    "Order placed successfully! Your order ID is: " + orderId);
            
            // Redirect to view order details
            response.sendRedirect(request.getContextPath() + 
                    "/OrderServlet?action=view&orderId=" + orderId);
        } else {
            // Failed to create order
            request.getSession().setAttribute("errorMessage", 
                    "Failed to place order. Please make sure your cart is not empty.");
            
            // Redirect to cart page
            response.sendRedirect(request.getContextPath() + "/pages/user/Cart.jsp");
        }
    }
}