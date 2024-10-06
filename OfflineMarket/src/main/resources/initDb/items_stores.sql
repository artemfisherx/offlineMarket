create table items_stores
(
	item int references items on delete cascade,
	store int references stores on delete cascade,
	balance int not null,
	check(balance>=0),
	primary key(item, store)
)