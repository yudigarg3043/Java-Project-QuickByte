create database javaproject2;
use javaproject2;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    items TEXT NOT NULL,
    address TEXT NOT NULL,
    total INT NOT NULL,
    order_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS food_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name varchar(100) NOT NULL,
    price INT NOT NULL
);

INSERT INTO food_items (name, price) VALUES
('Fries', 80),
('Cold Coffee', 120),
('Burger', 150),
('Garlic Bread', 150),
('Pasta', 200),
('Pizza', 250);


show tables;

select * from users;

select * from orders;
