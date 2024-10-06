CREATE TRIGGER after_delete_order_positions_trigger
AFTER DELETE on order_positions
FOR EACH ROW
EXECUTE FUNCTION after_delete_order_positions_func();