-- V2__insert_initial_data.sql

-- Insert users
INSERT INTO users (name, surname, email, password, phone_number, role) VALUES
('Admin', 'User', 'admin@example.com', 'adminpassword', '1234567890', 'ADMIN'),
('Regular', 'User', 'user@example.com', 'userpassword', '0987654321', 'USER');

-- Insert products
INSERT INTO products (description, picture, price, type) VALUES
('Product 1 Description', 'picture1.png', 1000, 'FREEZER'),
('Product 2 Description', 'picture2.png', 2000, 'OTHER'),
('Product 3 Description', 'picture3.png', 3000, 'FRIDGE');

-- Insert offers with status
INSERT INTO offers (title, user_id, status) VALUES
('Offer for Admin (ACTIVE)', 1, 'ACTIVE'),
('Offer for User (CANCELED)', 2, 'CANCELED'),
('Another Offer for Admin (FINISHED)', 1, 'FINISHED');

-- Add relation between offer and products (offer_products)
INSERT INTO offer_products (offer_id, product_id) VALUES
(1, 1), -- Offer 1 (Admin) has Product 1
(1, 2), -- Offer 1 (Admin) has Product 2
(2, 2), -- Offer 2 (User) has Product 2
(2, 3), -- Offer 2 (User) has Product 3
(3, 1), -- Offer 3 (Admin) has Product 1
(3, 3); -- Offer 3 (Admin) has Product 3