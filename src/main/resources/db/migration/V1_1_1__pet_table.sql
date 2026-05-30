CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS pet
(
    id         UUID PRIMARY KEY     DEFAULT uuid_generate_v4(),
    name       VARCHAR     NOT NULL,
    age        INT         NOT NULL,
    type       varchar(16) NOT NULL,
    is_deleted BOOLEAN     NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP   NOT NULL DEFAULT now(),
    deleted_at TIMESTAMP   NULL
);

CREATE INDEX IF NOT EXISTS pet_name_created_deleted_idx ON pet (name, created_at, is_deleted);
CREATE INDEX IF NOT EXISTS pet_name_age_created_deleted_idx ON pet (name, age, created_at, is_deleted);
CREATE INDEX IF NOT EXISTS pet_name_type_created_deleted_idx ON pet (name, type, created_at, is_deleted);
CREATE INDEX IF NOT EXISTS pet_name_age_type_created_deleted_idx ON pet (name, age, type, created_at, is_deleted);
