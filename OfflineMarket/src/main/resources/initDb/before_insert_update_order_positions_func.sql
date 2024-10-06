create function before_insert_update_order_positions_func() RETURNS trigger
as $$
DECLARE
bal int;
cur_count int;
BEGIN
SELECT balance from items_stores WHERE item=NEW.item AND store=NEW.store INTO bal;
SELECT "count" from order_positions WHERE "id"=NEW.id INTO cur_count;

IF cur_count IS NULL THEN
	IF bal>=NEW.count THEN
		UPDATE items_stores SET balance=bal-NEW.count WHERE item=NEW.item AND store=NEW.store;
		RETURN NEW;
	ELSE 
		RETURN NULL;
	END IF;
ELSE
	IF cur_count > NEW.count THEN
		UPDATE items_stores SET balance=bal+cur_count-NEW.count WHERE item=NEW.item AND store=NEW.store;	
		RETURN NEW;
	ELSIF cur_count<NEW.count THEN
		IF bal>=NEW.count-cur_count THEN
			UPDATE items_stores SET balance=bal+cur_count-NEW.count WHERE item=NEW.item AND store=NEW.store;		
			RETURN NEW;
		ELSE
			RETURN NULL;
		END IF;
	ELSE
		RETURN NULL;
	END IF;
END IF;

END
$$
language plpgsql;