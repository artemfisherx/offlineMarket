CREATE TABLE IF NOT EXISTS items
(
	"id" serial primary key,
	"item_number" text,
	"name" text,
	"manufacturer" int references counteragents not null,
	"supplier" int references counteragents not null,
	"image" text,
	"item_type" int not null,
	"balance" int,
	check(balance>=0)
);