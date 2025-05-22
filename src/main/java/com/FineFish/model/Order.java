package com.FineFish.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Model class representing an order
 */
public class Order {
    private int orderId;
    private int userId;
    private String username;
    private Date orderDate;
    private double totalAmount;
    private List<OrderItem> orderItems;
    
    /**
     * Default constructor
     */
    public Order() {
        this.orderItems = new ArrayList<>();
        
    }
    
    /**
     * Constructor with basic information
     * 
     * @param orderId Order ID
     * @param userId User ID
     * @param username Username
     * @param orderDate Order date
     * @param totalAmount Total amount
     */
    public Order(int orderId, int userId, String username, Date orderDate, double totalAmount) {
        this.orderId = orderId;
        this.userId = userId;
        this.username = username;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.orderItems = new ArrayList<>();
     }
    
    /**
     * Get the order ID
     * 
     * @return order ID
     */
    public int getOrderId() {
        return orderId;
    }
    
    /**
     * Set the order ID
     * 
     * @param orderId the order ID to set
     */
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    
    /**
     * Get the user ID
     * 
     * @return user ID
     */
    public int getUserId() {
        return userId;
    }
    
    /**
     * Set the user ID
     * 
     * @param userId the user ID to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    /**
     * Get the username
     * 
     * @return username
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Set the username
     * 
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * Get the order date
     * 
     * @return order date
     */
    public Date getOrderDate() {
        return orderDate;
    }
    
    /**
     * Set the order date
     * 
     * @param orderDate the order date to set
     */
    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
    
    /**
     * Get the total amount
     * 
     * @return total amount
     */
    public double getTotalAmount() {
        return totalAmount;
    }
    
    /**
     * Set the total amount
     * 
     * @param totalAmount the total amount to set
     */
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
        
    /**
     * Get the order items
     * 
     * @return list of order items
     */
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
    
    /**
     * Set the order items
     * 
     * @param orderItems the list of order items to set
     */
    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
    
    /**
     * Add an order item
     * 
     * @param item the order item to add
     */
    public void addOrderItem(OrderItem item) {
        this.orderItems.add(item);
    }
    
    /**
     * String representation of the order
     */
    @Override
    public String toString() {
        return "Order [orderId=" + orderId + ", userId=" + userId + ", username=" + username
               + ", orderDate=" + orderDate + ", totalAmount=" + totalAmount + "]";
    }
    }