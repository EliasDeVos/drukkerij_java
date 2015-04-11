-- Function: drukorder_update()

-- DROP FUNCTION drukorder_update();

CREATE OR REPLACE FUNCTION drukorder_update()
  RETURNS trigger AS
$BODY$
    BEGIN
	PERFORM pg_notify( cast('updatedrukorder' as text), cast(NEW.drukitemid as text));
        RETURN NULL;
    END; 
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION drukorder_update()
  OWNER TO postgres;
