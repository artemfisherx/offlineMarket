CREATE FUNCTION after_update_receipts_func() RETURNS trigger
AS
$$
BEGIN
UPDATE items_stores SET balance=balance-OLD.count 
WHERE item=OLD.item AND store=OLD.store;
RETURN NEW;
END;
$$
language plpgsql;
