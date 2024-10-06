CREATE TABLE IF NOT EXISTS counteragents
(
	"id" serial primary key,
	"inn" bigint,
	"name" text,
	"country" text,
	"city" text,
	"street" text,
	"building" text,
	check (inn <= '999999999999')
);

INSERT INTO counteragents(inn, name, country, city, street, building)
VALUES ('7728495457', 'Schit i mech','Russia', 'Moscow', 'Butlerova', '17'),
('770506732316', 'IP Voronov I V', 'Russia', 'Moscow', 'Nikitina', '10'),
('7722498691', 'Automir', 'Russia', 'Nizhniy Novgorod', 'Talalikhina', '1');