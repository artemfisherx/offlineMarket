CREATE FUNCTION after_delete_receipts_func() RETURNS trigger
AS $$
BEGIN
UPDATE items_stores SET balance=balance-OLD.count 
WHERE item=OLD.item AND store=OLD.store;
RETURN OLD;
END;
$$
language plpgsql;