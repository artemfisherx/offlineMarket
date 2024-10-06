create table orders
(
	id serial primary key,
	seller int references employees,
	client int references counteragents,
	datetime timestamptz
)