CREATE FUNCTION movement_and_balance_by_date(dt date)
RETURNS table(item int, "count" int, datetime timestamptz, oper text, balance int)
AS $$
DECLARE
cur_balance int;
cur CURSOR(dte date) FOR select ordp.item, ordp.count, ord.datetime, 'out' as "oper" from orders ord
join order_positions ordp
on ord.id=ordp.order
where ord.datetime::date<=dte 
union
select rec.id, rec.count, rec.datetime, 'in' as "oper" from receipts rec
where rec.datetime::date<=dte 
order by datetime; 
BEGIN

cur_balance=0;
FOR r IN cur(dt) LOOP
	IF(r.oper='in') THEN
		cur_balance=cur_balance+r.count;
	ELSE
		cur_balance=cur_balance-r.count;
	END IF;
	item= r.item;
	"count" = r.count;
	datetime = r.datetime;
	oper = r.oper;
	balance = cur_balance;
	RETURN NEXT;
END LOOP;

RETURN;

END;
$$
language plpgsql;