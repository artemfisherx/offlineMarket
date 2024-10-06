CREATE FUNCTION movement_by_date(dt date)
RETURNS table(item int, "count" int, datetime timestamptz, oper text)
AS $$
BEGIN
RETURN QUERY select ordp.item, ordp.count, ord.datetime, 'out' as "oper" from orders ord
join order_positions ordp
on ord.id=ordp.order
where ord.datetime::date<='2025-01-1' 
union
select rec.id, rec.count, rec.datetime, 'in' as "oper" from receipts rec
where rec.datetime::date<='2025-01-1' 
order by datetime; 
END;
$$
language plpgsql;