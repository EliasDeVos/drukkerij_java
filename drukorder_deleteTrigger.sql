-- Trigger: drukorder_delete on drukitems

-- DROP TRIGGER drukorder_delete ON drukitems;

CREATE TRIGGER drukorder_delete
  AFTER DELETE
  ON drukitems
  FOR EACH ROW
  EXECUTE PROCEDURE drukorder_delete();
