-- Insert users
INSERT INTO users (name, surname, email, password, phone_number, role) VALUES
('Admin', 'User', 'admin@example.com', 'adminpassword', '1234567890', 'ADMIN'),
('Regular', 'User', 'user@example.com', 'userpassword', '0987654321', 'USER');

-- Insert products
INSERT INTO products (description, picture, price, type) VALUES
('Product 1 Description', 'picture1.png', 1000, 'FREEZER'),
('Product 2 Description', 'picture2.png', 2000, 'FREEZER'),
('Product 3 Description', 'picture3.png', 3000, 'FRIDGE');

-- Insert offers
INSERT INTO offers (title, user_id) VALUES
('Offer for Admin', 1),
('Offer for User', 2);

-- Add relation between offer and products (offer_products)
INSERT INTO offer_products (offer_id, product_id) VALUES
(1, 1),
(1, 2),
(2, 2),
(2, 3);
