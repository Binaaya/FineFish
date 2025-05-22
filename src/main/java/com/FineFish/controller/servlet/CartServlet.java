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

import com.FineFish.controller.dao.CartDAO;
import com.FineFish.model.Cart;
import com.FineFish.model.CartItem;
import com.FineFish.model.User;

/**
 * Servlet implementation class CartServlet
 * Handles cart operations (display, add, update, remove, clear)
 */
@WebServlet("/CartServlet")
public class CartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * Default constructor
     */
    public CartServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /**
     * Process both GET and POST requests
     */
    private void processRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            // If not logged in, redirect to login page with a message
            session.setAttribute("errorMessage", "Please log in to view your cart");
            response.sendRedirect(request.getContextPath() + "/LogInController");
            return;
        }
        
        // Get action parameter
        String action = request.getParameter("action");
        
        try {
            if ("view".equals(action) || action == null) {
                // View cart - default action
                viewCart(request, response, user.getUserId());
            } else if ("add".equals(action)) {
                // Add to cart
                addToCart(request, response, user.getUserId());
            } else if ("update".equals(action)) {
                // Update cart item
                updateCartItem(request, response);
            } else if ("increase".equals(action)) {
                // Increase quantity by 1
                increaseQuantity(request, response);
            } else if ("decrease".equals(action)) {
                // Decrease quantity by 1
                decreaseQuantity(request, response);
            } else if ("remove".equals(action)) {
                // Remove from cart
                removeFromCart(request, response);
            } else if ("clear".equals(action)) {
                // Clear cart
                clearCart(request, response, user.getUserId());
            } else {
                // Invalid action, redirect to cart page
                response.sendRedirect(request.getContextPath() + "/CartServlet?action=view");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "Error processing cart operation: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/CartServlet?action=view");
        }
    }
    
    /**
     * View cart items
     */
    private void viewCart(HttpServletRequest request, HttpServletResponse response, int userId) 
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        CartDAO cartDAO = null;
        try {
            cartDAO = new CartDAO();
            
            // Get cart items
            List<CartItem> cartItems = cartDAO.getCartItems(userId);
            
            // Create cart object
            Cart cart = new Cart(userId);
            cart.setCartProducts(cartItems);
            
            // Set cart in session
            request.getSession().setAttribute("cart", cart);
            
            // Set cart count in session - unique items count
            request.getSession().setAttribute("cartCount", cartItems.size());
            
            // Forward to cart page
            request.getRequestDispatcher("/pages/user/Cart.jsp").forward(request, response);
        } finally {
            if (cartDAO != null) {
                cartDAO.closeConnection();
            }
        }
    }
    
    /**
     * Add item to cart
     */
    private void addToCart(HttpServletRequest request, HttpServletResponse response, int userId) 
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        // Get parameters
        String productIdStr = request.getParameter("productId");
        String quantityStr = request.getParameter("quantity");
        
        // Validate parameters
        if (productIdStr == null || productIdStr.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "Product ID is required");
            response.sendRedirect(request.getContextPath() + "/CartServlet?action=view");
            return;
        }
        
        int productId;
        int quantity = 1; // Default quantity
        
        try {
            productId = Integer.parseInt(productIdStr);
            
            if (quantityStr != null && !quantityStr.trim().isEmpty()) {
                quantity = Integer.parseInt(quantityStr);
                if (quantity <= 0) {
                    request.getSession().setAttribute("errorMessage", "Quantity must be greater than zero");
                    response.sendRedirect(request.getContextPath() + "/CartServlet?action=view");
                    return;
                }
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Invalid product ID or quantity");
            response.sendRedirect(request.getContextPath() + "/CartServlet?action=view");
            return;
        }
        
        CartDAO cartDAO = null;
        try {
            cartDAO = new CartDAO();
            
            // Add to cart
            boolean success = cartDAO.addToCart(userId, productId, quantity);
            
            if (success) {
                // Update session cart with a new DAO
                updateSessionCart(request, userId);
                
                request.getSession().setAttribute("successMessage", "Product added to cart successfully");
            } else {
                request.getSession().setAttribute("errorMessage", "Failed to add product to cart. Not enough quantity available.");
            }
            
            // Get the referer URL to go back to the previous page
            String referer = request.getHeader("Referer");
            if (referer != null && !referer.isEmpty()) {
                response.sendRedirect(referer);
            } else {
                response.sendRedirect(request.getContextPath() + "/CartServlet?action=view");
            }
        } finally {
            if (cartDAO != null) {
                cartDAO.closeConnection();
            }
        }
    }
    
    /**
     * Increase quantity by 1
     */
    private void increaseQuantity(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException, ClassNotFoundException {
        // Get parameters
        String cartItemIdStr = request.getParameter("cartItemId");
        
        if (cartItemIdStr == null || cartItemIdStr.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "Cart item ID is required");
            response.sendRedirect(request.getContextPath() + "/CartServlet?action=view");
            return;
        }
        
        int cartItemId;
        
        try {
            cartItemId = Integer.parseInt(cartItemIdStr);
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Invalid cart item ID");
            response.sendRedirect(request.getContextPath() + "/CartServlet?action=view");
            return;
        }
        
        // Get user from session
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            session.setAttribute("errorMessage", "Please log in to update your cart");
            response.sendRedirect(request.getContextPath() + "/LogInController");
            return;
        }
        
        CartDAO cartDAO = null;
        try {
            cartDAO = new CartDAO();
            
            // Get current quantity from cart items
            List<CartItem> cartItems = cartDAO.getCartItems(user.getUserId());
            
            for (CartItem item : cartItems) {
                if (item.getCartItemId() == cartItemId) {
                    int currentQuantity = item.getQuantity();
                    int newQuantity = currentQuantity + 1;
                    
                    // Ensure we don't exceed available quantity
                    if (newQuantity <= item.getAvailableQuantity()) {
                        // Update quantity with a new DAO to avoid connection issues
                        cartDAO.closeConnection();
                        cartDAO = new CartDAO();
                        boolean success = cartDAO.updateCartItemQuantity(cartItemId, newQuantity);
                        
                        if (success) {
                            // Update session cart with another new DAO
                            updateSessionCart(request, user.getUserId());
                            session.setAttribute("successMessage", "Cart updated successfully");
                        } else {
                            session.setAttribute("errorMessage", "Failed to update cart");
                        }
                    } else {
                        session.setAttribute("errorMessage", "Cannot exceed available quantity");
                    }
                    
                    break;
                }
            }
            
            response.sendRedirect(request.getContextPath() + "/CartServlet?action=view");
        } finally {
            if (cartDAO != null) {
                cartDAO.closeConnection();
            }
        }
    }
    
    /**
     * Decrease quantity by 1
     */
    private void decreaseQuantity(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException, ClassNotFoundException {
        // Get parameters
        String cartItemIdStr = request.getParameter("cartItemId");
        
        if (cartItemIdStr == null || cartItemIdStr.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "Cart item ID is required");
            response.sendRedirect(request.getContextPath() + "/CartServlet?action=view");
            return;
        }
        
        int cartItemId;
        
        try {
            cartItemId = Integer.parseInt(cartItemIdStr);
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Invalid cart item ID");
            response.sendRedirect(request.getContextPath() + "/CartServlet?action=view");
            return;
        }
        
        // Get user from session
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            session.setAttribute("errorMessage", "Please log in to update your cart");
            response.sendRedirect(request.getContextPath() + "/LogInController");
            return;
        }
        
        CartDAO cartDAO = null;
        try {
            cartDAO = new CartDAO();
            
            // Get current quantity from cart items
            List<CartItem> cartItems = cartDAO.getCartItems(user.getUserId());
            
            for (CartItem item : cartItems) {
                if (item.getCartItemId() == cartItemId) {
                    int currentQuantity = item.getQuantity();
                    
                    // If quantity is 1, remove the item
                    if (currentQuantity <= 1) {
                        // Close and create new DAO to avoid connection issues
                        cartDAO.closeConnection();
                        cartDAO = new CartDAO();
                        boolean success = cartDAO.removeCartItem(cartItemId);
                        
                        if (success) {
                            // Update session cart with another new DAO
                            updateSessionCart(request, user.getUserId());
                            session.setAttribute("successMessage", "Item removed from cart successfully");
                        } else {
                            session.setAttribute("errorMessage", "Failed to remove item from cart");
                        }
                    } else {
                        // Decrease quantity by 1
                        int newQuantity = currentQuantity - 1;
                        // Close and create new DAO to avoid connection issues
                        cartDAO.closeConnection();
                        cartDAO = new CartDAO();
                        boolean success = cartDAO.updateCartItemQuantity(cartItemId, newQuantity);
                        
                        if (success) {
                            // Update session cart with another new DAO
                            updateSessionCart(request, user.getUserId());
                            session.setAttribute("successMessage", "Cart updated successfully");
                        } else {
                            session.setAttribute("errorMessage", "Failed to update cart");
                        }
                    }
                    
                    break;
                }
            }
            
            response.sendRedirect(request.getContextPath() + "/CartServlet?action=view");
        } finally {
            if (cartDAO != null) {
                cartDAO.closeConnection();
            }
        }
    }
    
    /**
     * Update cart item
     */ 
    private void updateCartItem(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        // Get parameters
        String cartItemIdStr = request.getParameter("cartItemId");
        String quantityStr = request.getParameter("quantity");
        
        // Validate parameters
        if (cartItemIdStr == null || cartItemIdStr.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "Cart item ID is required");
            response.sendRedirect(request.getContextPath() + "/CartServlet?action=view");
            return;
        }
        
        if (quantityStr == null || quantityStr.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "Quantity is required");
            response.sendRedirect(request.getContextPath() + "/CartServlet?action=view");
            return;
        }
        
        int cartItemId;
        int quantity;
        
        try {
            cartItemId = Integer.parseInt(cartItemIdStr);
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Invalid cart item ID or quantity");
            response.sendRedirect(request.getContextPath() + "/CartServlet?action=view");
            return;
        }
        
        // Get user from session
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            session.setAttribute("errorMessage", "Please log in to update your cart");
            response.sendRedirect(request.getContextPath() + "/LogInController");
            return;
        }
        
        CartDAO cartDAO = null;
        try {
            cartDAO = new CartDAO();
            
            // Update cart item
            boolean success = cartDAO.updateCartItemQuantity(cartItemId, quantity);
            
            if (success) {
                // Update session cart with a new DAO
                updateSessionCart(request, user.getUserId());
                
                // Set success message
                session.setAttribute("successMessage", "Cart updated successfully");
            } else {
                session.setAttribute("errorMessage", "Failed to update cart. Not enough quantity available.");
            }
            
            response.sendRedirect(request.getContextPath() + "/CartServlet?action=view");
        } finally {
            if (cartDAO != null) {
                cartDAO.closeConnection();
            }
        }
    }
    
    /**
     * Helper method to update session cart
     */
    private void updateSessionCart(HttpServletRequest request, int userId) throws SQLException, ClassNotFoundException {
        HttpSession session = request.getSession();
        
        CartDAO cartDAO = null;
        try {
            cartDAO = new CartDAO();
            
            // Get updated cart items
            List<CartItem> cartItems = cartDAO.getCartItems(userId);
            
            // Update cart count in session - count of unique items
            session.setAttribute("cartCount", cartItems.size());
            
            // Create a new cart with the updated items
            Cart cart = new Cart(userId);
            cart.setCartProducts(cartItems);
            session.setAttribute("cart", cart);
        } finally {
            if (cartDAO != null) {
                cartDAO.closeConnection();
            }
        }
    }
    
    /**
     * Remove item from cart
     */
    private void removeFromCart(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        // Get parameters
        String cartItemIdStr = request.getParameter("cartItemId");
        
        // Validate parameters
        if (cartItemIdStr == null || cartItemIdStr.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "Cart item ID is required");
            response.sendRedirect(request.getContextPath() + "/CartServlet?action=view");
            return;
        }
        
        int cartItemId;
        
        try {
            cartItemId = Integer.parseInt(cartItemIdStr);
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Invalid cart item ID");
            response.sendRedirect(request.getContextPath() + "/CartServlet?action=view");
            return;
        }
        
        // Get user from session
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            session.setAttribute("errorMessage", "Please log in to update your cart");
            response.sendRedirect(request.getContextPath() + "/LogInController");
            return;
        }
        
        CartDAO cartDAO = null;
        try {
            cartDAO = new CartDAO();
            
            // Remove from cart
            boolean success = cartDAO.removeCartItem(cartItemId);
            
            if (success) {
                // Update session cart with a new DAO
                updateSessionCart(request, user.getUserId());
                
                session.setAttribute("successMessage", "Item removed from cart successfully");
            } else {
                session.setAttribute("errorMessage", "Failed to remove item from cart");
            }
            
            response.sendRedirect(request.getContextPath() + "/CartServlet?action=view");
        } finally {
            if (cartDAO != null) {
                cartDAO.closeConnection();
            }
        }
    }
    
    /**
     * Clear cart
     */
    private void clearCart(HttpServletRequest request, HttpServletResponse response, int userId) 
            throws SQLException, IOException, ClassNotFoundException {
        CartDAO cartDAO = null;
        try {
            cartDAO = new CartDAO();
            
            // Clear cart
            boolean success = cartDAO.clearCart(userId);
            
            if (success) {
                // Update cart count in session
                request.getSession().setAttribute("cartCount", 0);
                
                // Create empty cart
                Cart cart = new Cart(userId);
                request.getSession().setAttribute("cart", cart);
                
                // Using PRG pattern to redirect after POST to avoid form resubmission
                request.getSession().setAttribute("successMessage", "Cart cleared successfully");
            } else {
                request.getSession().setAttribute("errorMessage", "Failed to clear cart");
            }
            
            response.sendRedirect(request.getContextPath() + "/CartServlet?action=view");
        } finally {
            if (cartDAO != null) {
                cartDAO.closeConnection();
            }
        }
    }
}