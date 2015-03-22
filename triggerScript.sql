--Database: testdb

-- DROP DATABASE testdb;
CREATE TRIGGER contact_notify
  AFTER INSERT OR UPDATE
  ON CONTACTS
  FOR EACH ROW
  EXECUTE PROCEDURE contact_notify();

  
  CREATE OR REPLACE FUNCTION contact_notify()
  RETURNS trigger AS
$BODY$
    BEGIN
        NOTIFY mymessage, 'freli rij';
        --NOTIFY mymessage, 'fired by NezY';
        RETURN NULL;
    END; 
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION contact_notify() OWNER TO postgres;