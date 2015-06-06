Create table NODE(
  id INT UNSIGNED,
  lattitude DECIMAL(20,10) NOT NULL,
  longtitude DECIMAL(20,10) NOT NULL,
  deleted VARCHAR(1) NOT NULL,
  PRIMARY KEY(id),
  CONSTRAINT CHK_DELETED_NODE CHECK (deleted in ('Y', 'N'))
);


CREATE TABLE Edge(
  ID INT UNSIGNED NOT NULL AUTO_INCREMENT,
  BEGIN_ID INT UNSIGNED NOT NULL,
  DEST_ID INT UNSIGNED NOT NULL,
  FOREIGN KEY (BEGIN_ID) REFERENCES NODE(id),
  FOREIGN KEY (DEST_ID) REFERENCES NODE(id),
  PRIMARY KEY(ID)
);

Create table WAY(
  id INT UNSIGNED,
  deleted VARCHAR(1) NOT NULL,
  PRIMARY KEY(id),
  CONSTRAINT CHK_DELETED CHECK (deleted in ('Y', 'N'))
);


CREATE TABLE NODE_WAY(
  NODE_ID INT UNSIGNED NOT NULL,
  WAY_ID INT UNSIGNED NOT NULL,
  ORDERIDX INT UNSIGNED NOT NULL,
  FOREIGN KEY (NODE_ID) REFERENCES NODE(id),
  FOREIGN KEY (WAY_ID) REFERENCES WAY(id),
  CONSTRAINT ND_WAY UNIQUE (node_id, way_id),
  constraint UNIQUE_ORDER UNIQUE(way_id, orderidx)
);

CREATE INDEX WAY_FK ON NODE_WAY (way_id) USING BTREE;
CREATE INDEX WNODE_FK ON NODE_WAY (node_id) USING BTREE;


CREATE TRIGGER CHECK_EDGE_ORDER_ON_UPDATE
BEFORE UPDATE ON EDGE
FOR EACH ROW
BEGIN
    DECLARE temp int(2);
    select count(*) into temp 
      from edge where 
        (begin_id=NEW.begin_ID and DEST_ID=NEW.dest_ID) or
        (begin_id = NEW.DEST_ID and DEST_ID = NEW.BEGIN_ID);
        
    IF (temp <> 0) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Edge is already declared with other';
    END IF;

END

CREATE TRIGGER CHECK_EDGE_ORDER_ON_INSERT
BEFORE INSERT ON EDGE
FOR EACH ROW
BEGIN
    DECLARE temp int(2);
    select count(*) into temp 
      from edge where 
        (begin_id=NEW.begin_ID and DEST_ID=NEW.dest_ID) or
        (begin_id = NEW.DEST_ID and DEST_ID = NEW.BEGIN_ID);
        
    IF (temp <> 0) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Edge is already declared with other';
    END IF;

END

commit;