CREATE FUNCTION after_delete_order_positions_func() RETURNS trigger
as $$
DECLARE
bal int;
BEGIN
SELECT balance FROM items_stores WHERE item=OLD.item  AND store=OLD.store INTO bal;
bal=bal+OLD.count;
UPDATE items_stores SET balance = bal WHERE item=OLD.item AND store=OLD.store;
RETURN OLD;
END
$$
language plpgsql;