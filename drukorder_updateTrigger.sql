-- Trigger: drukorder_update on drukitems

-- DROP TRIGGER drukorder_update ON drukitems;

CREATE TRIGGER drukorder_update
  AFTER UPDATE
  ON drukitems
  FOR EACH ROW
  EXECUTE PROCEDURE drukorder_update();
