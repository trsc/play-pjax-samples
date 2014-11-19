# anythings schema

# --- !Ups

CREATE TABLE anythings (
  id BIGSERIAL PRIMARY KEY,
  value TEXT NOT NULL
);

# --- !Downs

DROP TABLE anythings;

