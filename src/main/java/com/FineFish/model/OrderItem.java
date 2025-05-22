package com.FineFish.model;

/**
 * Model class representing an item in an order
 */
public class OrderItem {
    private int orderItemId;
    private int orderId;
    private int productId;
    private String productName;
    private double price;
    private int quantity;
    private double subtotal;
    private String imageUrl;
    private String category;
    
    /**
     * Default constructor
     */
    public OrderItem() {
    }
    
    /**
     * Constructor with parameters
     * 
     * @param orderItemId Order item ID
     * @param orderId Order ID
     * @param productId Product ID
     * @param productName Product name
     * @param price Price
     * @param quantity Quantity
     * @param imageUrl Image URL
     * @param category Product category
     */
    public OrderItem(int orderItemId, int orderId, int productId, String productName,
                     double price, int quantity, String imageUrl, String category) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.subtotal = price * quantity;
        this.imageUrl = imageUrl;
        this.category = category;
    }
    
    /**
     * Get the order item ID
     * 
     * @return order item ID
     */
    public int getOrderItemId() {
        return orderItemId;
    }
    
    /**
     * Set the order item ID
     * 
     * @param orderItemId the order item ID to set
     */
    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
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
     * Get the product ID
     * 
     * @return product ID
     */
    public int getProductId() {
        return productId;
    }
    
    /**
     * Set the product ID
     * 
     * @param productId the product ID to set
     */
    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    /**
     * Get the product name
     * 
     * @return product name
     */
    public String getProductName() {
        return productName;
    }
    
    /**
     * Set the product name
     * 
     * @param productName the product name to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    /**
     * Get the price
     * 
     * @return price
     */
    public double getPrice() {
        return price;
    }
    
    /**
     * Set the price
     * 
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
        this.subtotal = price * quantity;
    }
    
    /**
     * Get the quantity
     * 
     * @return quantity
     */
    public int getQuantity() {
        return quantity;
    }
    
    /**
     * Set the quantity
     * 
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.subtotal = price * quantity;
    }
    
    /**
     * Get the subtotal
     * 
     * @return subtotal
     */
    public double getSubtotal() {
        return subtotal;
    }
    
    /**
     * Set the subtotal
     * 
     * @param subtotal the subtotal to set
     */
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    
    /**
     * Get the image URL
     * 
     * @return image URL
     */
    public String getImageUrl() {
        return imageUrl;
    }
    
    /**
     * Set the image URL
     * 
     * @param imageUrl the image URL to set
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    /**
     * Get the category
     * 
     * @return category
     */
    public String getCategory() {
        return category;
    }
    
    /**
     * Set the category
     * 
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }
    
    /**
     * String representation of the order item
     */
    @Override
    public String toString() {
        return "OrderItem [orderItemId=" + orderItemId + ", orderId=" + orderId + ", productId=" + productId
                + ", productName=" + productName + ", price=" + price + ", quantity=" + quantity
                + ", subtotal=" + subtotal + "]";
    }
}