-- Function: drukorder_insert()

-- DROP FUNCTION drukorder_insert();

CREATE OR REPLACE FUNCTION drukorder_insert()
  RETURNS trigger AS
$BODY$
	declare val int := 2;
    BEGIN
	PERFORM pg_notify( cast('insertdrukorder' as text), cast(NEW.drukitemid as text));
        RETURN NULL;
    END; 
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION drukorder_insert()
  OWNER TO postgres;
