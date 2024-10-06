create table order_positions
(
	id serial primary key,
	item int references items,
	store int references stores,
	count int,
	order int references orders on delete cascade not null
)