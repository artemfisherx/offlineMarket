CREATE TRIGGER after_insert_receipts_trigger
AFTER INSERT OR UPDATE 
ON receipts
FOR EACH ROW
EXECUTE FUNCTION after_insert_receipts_func();