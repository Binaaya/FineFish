package com.FineFish.model;

import java.util.Date;

/**
 * Sale model class representing a sales record
 */
public class Sale {
    private int orderId;
    private int userId;
    private String username;
    private Date orderDate;
    private double amount;
    private int itemCount;
    
    /**
     * Default constructor
     */
    public Sale() {
    }
    
    /**
     * Constructor with parameters
     * 
     * @param orderId Order ID
     * @param userId User ID
     * @param username Username
     * @param orderDate Order date
     * @param amount Order amount
     * @param itemCount Number of items in order
     */
    public Sale(int orderId, int userId, String username, Date orderDate, double amount, int itemCount) {
        this.orderId = orderId;
        this.userId = userId;
        this.username = username;
        this.orderDate = orderDate;
        this.amount = amount;
        this.itemCount = itemCount;
    }

    /**
     * Get the order ID
     * 
     * @return the orderId
     */
    public int getOrderId() {
        return orderId;
    }

    /**
     * Set the order ID
     * 
     * @param orderId the orderId to set
     */
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    /**
     * Get the user ID
     * 
     * @return the userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Set the user ID
     * 
     * @param userId the userId to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Get the username
     * 
     * @return the username
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
     * @return the orderDate
     */
    public Date getOrderDate() {
        return orderDate;
    }

    /**
     * Set the order date
     * 
     * @param orderDate the orderDate to set
     */
    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    /**
     * Get the order amount
     * 
     * @return the amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Set the order amount
     * 
     * @param amount the amount to set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Get the number of items in the order
     * 
     * @return the itemCount
     */
    public int getItemCount() {
        return itemCount;
    }

    /**
     * Set the number of items in the order
     * 
     * @param itemCount the itemCount to set
     */
    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
    
    /**
     * String representation of the Sale object
     */
    @Override
    public String toString() {
        return "Sale [orderId=" + orderId + ", userId=" + userId + ", username=" + username + ", orderDate=" + orderDate
                + ", amount=" + amount + ", itemCount=" + itemCount + "]";
    }
}