--неправильно сделано, суммирует не по всем заказам, а для каждого отдельно
CREATE FUNCTION sales_by_period(st date, fn date)
RETURNS table(item int, summary bigint)
AS
$$
DECLARE
idd int;
BEGIN
FOR idd IN SELECT "id" FROM orders WHERE datetime::date>=st AND
datetime::date<=fn
LOOP
	RETURN QUERY SELECT orp.item, sum(orp.count) FROM order_positions orp
	WHERE "order"=idd GROUP BY orp.item;	
END LOOP;

RETURN;

END;
$$
language plpgsql;
