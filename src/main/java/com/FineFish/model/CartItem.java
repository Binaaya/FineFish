package com.FineFish.model;

import java.math.BigDecimal;

/**
 * Model class representing an item in the user's cart
 */
public class CartItem {
    private int cartItemId;
    private int productId;
    private String productName;
    private String productDescription;
    private String fullDescription; // Add this field for full product description
    private BigDecimal price;
    private int quantity;
    private BigDecimal subtotal;
    private String imageUrl;
    private int availableQuantity;
    
    /**
     * Default constructor
     */
    public CartItem() {
    }
    
    /**
     * Constructor with parameters
     * 
     * @param cartItemId Cart item ID
     * @param productId Product ID
     * @param productName Product name
     * @param productDescription Product description
     * @param price Unit price
     * @param quantity Quantity
     * @param imageUrl Image URL
     * @param availableQuantity Maximum available quantity
     */
    public CartItem(int cartItemId, int productId, String productName, String productDescription, 
                    BigDecimal price, int quantity, String imageUrl, int availableQuantity) {
        this.cartItemId = cartItemId;
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.availableQuantity = availableQuantity;
        calculateSubtotal();
    }
    
    /**
     * Calculate the subtotal (price * quantity)
     */
    public void calculateSubtotal() {
        if (price != null && quantity > 0) {
            this.subtotal = price.multiply(new BigDecimal(quantity));
        } else {
            this.subtotal = BigDecimal.ZERO;
        }
    }
    
    /**
     * Get the cart item ID
     * 
     * @return cart item ID
     */
    public int getCartItemId() {
        return cartItemId;
    }
    
    /**
     * Set the cart item ID
     * 
     * @param cartItemId the cart item ID to set
     */
    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
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
     * Get the product description
     * 
     * @return product description
     */
    public String getProductDescription() {
        return productDescription;
    }
    
    /**
     * Set the product description
     * 
     * @param productDescription the product description to set
     */
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
    
    /**
     * Get the full product description
     * 
     * @return full product description
     */
    public String getFullDescription() {
        return fullDescription;
    }
    
    /**
     * Set the full product description
     * 
     * @param fullDescription the full product description to set
     */
    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }
    
    /**
     * Get the price
     * 
     * @return price
     */
    public BigDecimal getPrice() {
        return price;
    }
    
    /**
     * Set the price
     * 
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
        calculateSubtotal();
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
        calculateSubtotal();
    }
    
    /**
     * Get the subtotal
     * 
     * @return subtotal
     */
    public BigDecimal getSubtotal() {
        return subtotal;
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
     * Get the available quantity
     * 
     * @return available quantity
     */
    public int getAvailableQuantity() {
        return availableQuantity;
    }
    
    /**
     * Set the available quantity
     * 
     * @param availableQuantity the available quantity to set
     */
    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }
    
    /**
     * String representation of the cart item
     */
    @Override
    public String toString() {
        return "CartItem [cartItemId=" + cartItemId + ", productId=" + productId + ", productName=" + productName
                + ", price=" + price + ", quantity=" + quantity + ", subtotal=" + subtotal + "]";
    }
}