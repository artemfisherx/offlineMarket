CREATE FUNCTION after_insert_receipts_func() RETURNS trigger
AS
$$
BEGIN
UPDATE items_stores SET balance=balance+NEW.count WHERE item=NEW.item AND store=NEW.store;
RETURN NEW;
END;
$$
language plpgsql;