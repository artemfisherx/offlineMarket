CREATE TABLE IF NOT EXISTS stores
(
	"id" serial primary key,
	"name" text
);


INSERT INTO stores (name) VALUES 
('The main store'), 
('The transit store'),
('The showcase store');
