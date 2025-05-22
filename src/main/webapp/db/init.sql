-- 1. User Table
CREATE TABLE `user` (
    User_id INT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(50) UNIQUE NOT NULL,
    Fname VARCHAR(50),
    Surname VARCHAR(50),
    Email VARCHAR(100) UNIQUE NOT NULL,
    Password VARCHAR(255) NOT NULL,
    Address TEXT,
    Phone_number VARCHAR(20),
    Role VARCHAR(20) DEFAULT 'user',
);

-- 2. Category Table
CREATE TABLE category (
    Category_id INT AUTO_INCREMENT PRIMARY KEY,
    Category_name VARCHAR(100) NOT NULL
);

-- 3. Species Table
CREATE TABLE species (
    Species_id INT AUTO_INCREMENT PRIMARY KEY,
    Category_id INT,
    Species_name VARCHAR(100) NOT NULL,
    Species_price DECIMAL(10, 2),
    Species_photo VARCHAR(255),
    Species_quantity INT,
    Species_description TEXT NOT NULL,
    FOREIGN KEY (Category_id) REFERENCES category(Category_id)
);

-- 4. Order Table
CREATE TABLE `order` (
    Order_id INT AUTO_INCREMENT PRIMARY KEY,
    User_id INT,
    Order_date DATE,
    Total_amount DECIMAL(10, 2),
    FOREIGN KEY (User_id) REFERENCES `user`(User_id)
);

-- 5. Order_species Table
CREATE TABLE order_species (
    Order_species_id INT AUTO_INCREMENT PRIMARY KEY,
    Order_id INT,
    Species_id INT,
    Price DECIMAL(10, 2),
    Quantity INT DEFAULT 1;,
    FOREIGN KEY (Order_id) REFERENCES `order`(Order_id),
    FOREIGN KEY (Species_id) REFERENCES species(Species_id)
);

-- 6. Cart Table
CREATE TABLE cart (
    Cart_id INT AUTO_INCREMENT PRIMARY KEY,
    User_id INT,
    Cart_quantity INT DEFAULT 1;
    FOREIGN KEY (User_id) REFERENCES `user`(User_id)
);

-- 7. Cart_species Table
CREATE TABLE cart_species (
    Cart_species_id INT AUTO_INCREMENT PRIMARY KEY,
    Cart_id INT,
    Species_id INT,
    FOREIGN KEY (Cart_id) REFERENCES cart(Cart_id),
    FOREIGN KEY (Species_id) REFERENCES species(Species_id)
);
