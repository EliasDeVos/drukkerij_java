-- Function: drukorder_delete()

-- DROP FUNCTION drukorder_delete();

CREATE OR REPLACE FUNCTION drukorder_delete()
  RETURNS trigger AS
$BODY$
    BEGIN
	PERFORM pg_notify('deletedrukorder', cast(OLD.drukitemid as text));
        RETURN NULL;
    END; 
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION drukorder_delete()
  OWNER TO postgres;
