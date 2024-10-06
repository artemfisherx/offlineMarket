CREATE FUNCTION sales_by_employee(emp int)
RETURNS SETOF record
AS $$
BEGIN
RETURN QUERY EXECUTE 'SELECT ordp.item, sum(ordp.count) 
FROM order_positions ordp
JOIN orders ord ON ordp.order=ord.id
WHERE ord.seller=$1
GROUP BY ordp.item'
USING emp;
END;
$$
language plpgsql;

