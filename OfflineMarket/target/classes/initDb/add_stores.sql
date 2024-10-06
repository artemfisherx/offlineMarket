CREATE PROCEDURE add_stores(text[])
AS $$
DECLARE
sts ALIAS FOR $1;
st text;
BEGIN

IF(array_length(sts,1) IS NULL) THEN
RAISE EXCEPTION 'Array is null';
RETURN;
END IF;

FOREACH st IN ARRAY sts LOOP
INSERT INTO stores("name") VALUES(st);
COMMIT;
END LOOP;

END;
$$
language plpgsql;