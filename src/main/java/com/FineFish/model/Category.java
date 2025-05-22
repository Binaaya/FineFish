
package com.FineFish.model;

/**
 * Category Model Class
 * Represents a category in the database
 */
public class Category {
    private int id;
    private String name;
    private int productCount; // Add product count field
    
    /**
     * Default constructor
     */
    public Category() {
    }
    
    /**
     * Constructor with parameters
     * 
     * @param id Category ID
     * @param name Category Name
     */
    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    /**
     * Constructor with parameters including product count
     * 
     * @param id Category ID
     * @param name Category Name
     * @param productCount Number of products in this category
     */
    public Category(int id, String name, int productCount) {
        this.id = id;
        this.name = name;
        this.productCount = productCount;
    }
    
    /**
     * Get the category ID
     * 
     * @return The category ID
     */
    public int getId() {
        return id;
    }
    
    /**
     * Set the category ID
     * 
     * @param id The category ID to set
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Get the category name
     * 
     * @return The category name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Set the category name
     * 
     * @param name The category name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Get the product count for this category
     * 
     * @return The number of products in this category
     */
    public int getProductCount() {
        return productCount;
    }
    
    /**
     * Set the product count for this category
     * 
     * @param productCount The number of products to set
     */
    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }
    
    /**
     * String representation of the Category
     */
    @Override
    public String toString() {
        return "Category [id=" + id + ", name=" + name + ", productCount=" + productCount + "]";
    }
}
