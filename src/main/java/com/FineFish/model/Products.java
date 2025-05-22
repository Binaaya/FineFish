package com.FineFish.model;

import java.math.BigDecimal;

/**
 * Products Model Class
 * Represents a product in the database
 */
public class Products {
    private int id;
    private String name;
    private int categoryId;
    private BigDecimal price;
    private String photo;
    private String categoryName;
    private int quantity; // Added field for product quantity
    private String description;

    /**
     * Default constructor
     */
    public Products() {
    }
    
    /**
     * Constructor with parameters
     * 
     * @param id Product ID
     * @param name Product Name
     * @param categoryId Category ID
     * @param price Product Price
     * @param photo Product Photo URL
     */
    public Products(int id, String name, int categoryId, BigDecimal price, String photo) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.price = price;
        this.photo = photo;
    }
    
    /**
     * Constructor with parameters including quantity
     * 
     * @param id Product ID
     * @param name Product Name
     * @param categoryId Category ID
     * @param price Product Price
     * @param photo Product Photo URL
     * @param quantity Product Quantity
     */
    public Products(int id, String name, int categoryId, BigDecimal price, String photo, int quantity) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.price = price;
        this.photo = photo;
        this.quantity = quantity;
    }
    
    /**
     * Get the product ID
     * 
     * @return The product ID
     */
    public int getId() {
        return id;
    }
    
    /**
     * Set the product ID
     * 
     * @param id The product ID to set
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Get the product name
     * 
     * @return The product name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Set the product name
     * 
     * @param name The product name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Get the category ID
     * 
     * @return The category ID
     */
    public int getCategoryId() {
        return categoryId;
    }
    
    /**
     * Set the category ID
     * 
     * @param categoryId The category ID to set
     */
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    
    /**
     * Get the product price
     * 
     * @return The product price
     */
    public BigDecimal getPrice() {
        return price;
    }
    
    /**
     * Set the product price
     * 
     * @param price The product price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    /**
     * Get the product photo URL
     * 
     * @return The product photo URL
     */
    public String getPhoto() {
        return photo;
    }
    
    /**
     * Set the product photo URL
     * 
     * @param photo The product photo URL to set
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    
    /**
     * Get the category name
     * 
     * @return The category name
     */
    public String getCategoryName() {
        return categoryName;
    }
    
    /**
     * Set the category name
     * 
     * @param categoryName The category name to set
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    /**
     * Get the product quantity
     * 
     * @return The product quantity
     */
    public int getQuantity() {
        return quantity;
    }
    
    /**
     * Set the product quantity
     * 
     * @param quantity The product quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    // getter and setter for description
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * String representation of the Product
     */
    @Override
    public String toString() {
        return "Products [id=" + id + ", name=" + name + ", categoryId=" + categoryId 
               + ", price=" + price + ", photo=" + photo + ", categoryName=" + categoryName 
               + ", quantity=" + quantity + "]";
    }
}