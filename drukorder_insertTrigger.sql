-- Trigger: drukorder_insert on drukitems

-- DROP TRIGGER drukorder_insert ON drukitems;

CREATE TRIGGER drukorder_insert
  AFTER INSERT
  ON drukitems
  FOR EACH ROW
  EXECUTE PROCEDURE drukorder_insert();
