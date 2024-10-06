CREATE TRIGGER before_insert_update_order_positions_trigger BEFORE INSERT OR UPDATE
ON order_positions
FOR EACH ROW
EXECUTE FUNCTION before_insert_update_order_positions_func();