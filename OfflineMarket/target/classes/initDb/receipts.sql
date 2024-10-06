create table receipts
(
	"id" serial primary key,
	datetime timestamptz,
	item int references items not null,
	store int references stores not null,
	"count" int not null,
	supplier int references counteragents not null,
	check("count">0)	
);