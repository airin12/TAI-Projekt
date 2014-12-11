-- Table: users

-- DROP TABLE users;

CREATE TABLE users
(
  username character varying(45) NOT NULL,
  password character varying(45),
  enabled boolean,
  CONSTRAINT users_pkey PRIMARY KEY (username)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE users
  OWNER TO postgres;

       
  
-- Table: user_roles

-- DROP TABLE user_roles;

CREATE TABLE user_roles
(
  user_role_id serial NOT NULL,
  username character varying(45),
  role character varying(45),
  CONSTRAINT user_roles_pkey PRIMARY KEY (user_role_id),
  CONSTRAINT user_roles_username_fkey FOREIGN KEY (username)
      REFERENCES users (username) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE user_roles
  OWNER TO postgres;
 
 
-- Table: analysis

-- DROP TABLE analysis;

CREATE TABLE analysis
(
  title character varying(45) NOT NULL,
  id serial NOT NULL,
  username character varying(45),
  CONSTRAINT analysis_pkey PRIMARY KEY (id),
  CONSTRAINT analysis_username_fkey FOREIGN KEY (username)
      REFERENCES users (username) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE analysis
  OWNER TO postgres;
  
-- Table: analysis_column

-- DROP TABLE analysis_column;

CREATE TABLE analysis_column
(
  id serial NOT NULL,
  analysisid serial NOT NULL,
  title character varying(45) NOT NULL,
  CONSTRAINT analysis_column_pkey PRIMARY KEY (id),
  CONSTRAINT analysis_column_analysis_id_fkey FOREIGN KEY (analysisid)
      REFERENCES analysis (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE analysis_column
  OWNER TO postgres;

-- Table: analysis_row

-- DROP TABLE analysis_row;

CREATE TABLE analysis_row
(
  id serial NOT NULL,
  analysisid serial NOT NULL,
  row_number integer,
  analysis_type character varying(45),
  CONSTRAINT analysis_row_pkey PRIMARY KEY (id),
  CONSTRAINT analysis_row_analysis_id_fkey FOREIGN KEY (analysisid)
      REFERENCES analysis (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE analysis_row
  OWNER TO postgres;

-- Table: analysis_value

-- DROP TABLE analysis_value;

CREATE TABLE analysis_value
(
  id serial NOT NULL,
  value bigint,
  rowid serial NOT NULL,
  columnid serial NOT NULL,
  string_value character varying(45),
  CONSTRAINT analysis_value_pkey PRIMARY KEY (id),
  CONSTRAINT analysis_value_column_id_fkey FOREIGN KEY (columnid)
      REFERENCES analysis_column (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT analysis_value_row_id_fkey FOREIGN KEY (rowid)
      REFERENCES analysis_row (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE analysis_value
  OWNER TO postgres;

