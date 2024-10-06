CREATE TRIGGER after_delete_receipts_trigger AFTER DELETE
ON receipts
FOR EACH ROW
EXECUTE FUNCTION after_delete_receipts_func();