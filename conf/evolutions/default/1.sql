# somethings schema

# --- !Ups

CREATE TABLE somethings (
  id BIGSERIAL PRIMARY KEY,
  value TEXT NOT NULL
);

# --- !Downs

DROP TABLE somethings;

