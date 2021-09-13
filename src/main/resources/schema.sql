DROP TABLE IF EXISTS log_entry;
CREATE TABLE log_entry
(
   id           INT            NOT NULL AUTO_INCREMENT,
   level        VARCHAR(10),
   timestamp    TIMESTAMP,
   message      VARCHAR(MAX),
   PRIMARY KEY (id)
);
