package com.FineFish.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class representing a user's shopping cart
 */
public class Cart {
    private int cartId;
    private int userId;
    private List<CartItem> cartProducts;
    private int itemCount;
    private BigDecimal totalAmount;
    
    /**
     * Default constructor
     */
    public Cart() {
        this.cartProducts = new ArrayList<>();
        this.totalAmount = BigDecimal.ZERO;
    }
    
    /**
     * Constructor with user ID
     * 
     * @param userId User ID
     */
    public Cart(int userId) {
        this.userId = userId;
        this.cartProducts = new ArrayList<>();
        this.totalAmount = BigDecimal.ZERO;
    }
    
    /**
     * Get the cart ID
     * 
     * @return cart ID
     */
    public int getCartId() {
        return cartId;
    }
    
    /**
     * Set the cart ID
     * 
     * @param cartId the cart ID to set
     */
    public void setCartId(int cartId) {
        this.cartId = cartId;
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
     * Get the cart products
     * 
     * @return list of cart items
     */
    public List<CartItem> getCartProducts() {
        return cartProducts;
    }
    
    /**
     * Set the cart products
     * 
     * @param cartProducts the list of cart items to set
     */
    public void setCartProducts(List<CartItem> cartProducts) {
        this.cartProducts = cartProducts;
        updateTotals();
    }
    
    /**
     * Add a product to the cart
     * 
     * @param product CartItem to add
     */
    public void addProduct(CartItem product) {
        // Check if product already exists in cart
        for (CartItem item : cartProducts) {
            if (item.getProductId() == product.getProductId()) {
                // Product already exists, update quantity
                item.setQuantity(item.getQuantity() + product.getQuantity());
                updateTotals();
                return;
            }
        }
        
        // Product doesn't exist in cart, add it
        cartProducts.add(product);
        updateTotals();
    }
    
    /**
     * Remove a product from the cart
     * 
     * @param productId Product ID to remove
     * @return true if removed, false if not found
     */
    public boolean removeProduct(int productId) {
        for (CartItem item : cartProducts) {
            if (item.getProductId() == productId) {
                cartProducts.remove(item);
                updateTotals();
                return true;
            }
        }
        return false;
    }
    
    /**
     * Update a product quantity
     * 
     * @param productId Product ID to update
     * @param quantity New quantity
     * @return true if updated, false if not found
     */
    public boolean updateProductQuantity(int productId, int quantity) {
        if (quantity <= 0) {
            return removeProduct(productId);
        }
        
        for (CartItem item : cartProducts) {
            if (item.getProductId() == productId) {
                item.setQuantity(quantity);
                updateTotals();
                return true;
            }
        }
        return false;
    }
    
    /**
     * Clear all products from the cart
     */
    public void clearCart() {
        cartProducts.clear();
        updateTotals();
    }
    
    /**
     * Update totals (item count and total amount)
     */
    private void updateTotals() {
        itemCount = 0;
        totalAmount = BigDecimal.ZERO;
        
        for (CartItem item : cartProducts) {
            itemCount += item.getQuantity();
            totalAmount = totalAmount.add(item.getSubtotal());
        }
    }
    
    /**
     * Get the item count
     * 
     * @return item count
     */
    public int getItemCount() {
        return itemCount;
    }
    
    /**
     * Get the total amount
     * 
     * @return total amount
     */
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    /**
     * String representation of the cart
     */
    @Override
    public String toString() {
        return "Cart [cartId=" + cartId + ", userId=" + userId + ", itemCount=" + itemCount + ", totalAmount=" + totalAmount + "]";
    }
}