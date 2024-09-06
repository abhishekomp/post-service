DROP SEQUENCE IF EXISTS post_seq;
CREATE SEQUENCE post_seq INCREMENT 1 START 101;

DROP TABLE IF EXISTS Post;
CREATE TABLE IF NOT EXISTS Post (
   id INT NOT NULL,
   user_id varchar(250) NOT NULL,
   title varchar(250) NOT NULL,
   body text NOT NULL,
   date_added date,
   is_blocked BOOLEAN DEFAULT FALSE,
   PRIMARY KEY (id)
);

