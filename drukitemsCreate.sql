-- Table: drukitems

-- DROP TABLE drukitems;

CREATE TABLE drukitems
(
  klant character varying(255),
  opdracht character varying(255),
  xpervel character varying(255),
  aantalnodig character varying(255),
  inschiet character varying(255),
  nmknb character varying(255),
  q character varying(255),
  zw character varying(255),
  zwaar4z2 character varying(255),
  glanzend character varying(255),
  helderheid character varying(255),
  soortpapier character varying(255),
  geplaatstdoor character varying(255),
  printer character varying(255),
  date character varying(255),
  opdrachtvoor character varying(255),
  prioriteit character varying(255),
  type character varying(255),
  drukitemid serial NOT NULL,
  commentaar character varying,
  commetaar character varying(255),
  afgewerkt character varying,
  CONSTRAINT drukitem_pk PRIMARY KEY (drukitemid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE drukitems
  OWNER TO postgres;

-- Trigger: drukorder_delete on drukitems

-- DROP TRIGGER drukorder_delete ON drukitems;

CREATE TRIGGER drukorder_delete
  AFTER DELETE
  ON drukitems
  FOR EACH ROW
  EXECUTE PROCEDURE drukorder_delete();

-- Trigger: drukorder_insert on drukitems

-- DROP TRIGGER drukorder_insert ON drukitems;

CREATE TRIGGER drukorder_insert
  AFTER INSERT
  ON drukitems
  FOR EACH ROW
  EXECUTE PROCEDURE drukorder_insert();

-- Trigger: drukorder_update on drukitems

-- DROP TRIGGER drukorder_update ON drukitems;

CREATE TRIGGER drukorder_update
  AFTER UPDATE
  ON drukitems
  FOR EACH ROW
  EXECUTE PROCEDURE drukorder_update();

