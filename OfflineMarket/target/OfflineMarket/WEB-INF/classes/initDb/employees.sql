CREATE TABLE IF NOT EXISTS employees
(
	"id" serial primary key,
	"name" text,
	"surname" text,
	"position" text
);

INSERT INTO employees ("name", "surname", "position") VALUES
('Natalya', 'Ivanova', 'cashier'),
('Petr', 'Loginov', 'director'),
('Ivan', 'Petrov', 'seller');