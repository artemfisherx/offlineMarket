CREATE trigger after_update_receipts_trigger 
AFTER UPDATE ON receipts
FOR EACH ROW
EXECUTE FUNCTION after_update_receipts_func();