CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS pet
(
    id         UUID PRIMARY KEY        DEFAULT uuid_generate_v4(),
    name       VARCHAR UNIQUE NOT NULL,
    age        INT            NOT NULL,
    type       varchar(16)    NOT NULL,
    created_at TIMESTAMP      NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS pet_name_idx ON pet (name);
CREATE INDEX IF NOT EXISTS pet_name_age_idx ON pet (name, age);
CREATE INDEX IF NOT EXISTS pet_name_age_idx ON pet (name, type);
CREATE INDEX IF NOT EXISTS pet_name_age_idx ON pet (type, age);
CREATE INDEX IF NOT EXISTS pet_name_age_idx ON pet (name, type, age);
