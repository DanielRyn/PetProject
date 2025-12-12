CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS pet
(
    id         UUID PRIMARY KEY     DEFAULT uuid_generate_v4(),
    name       VARCHAR(16) NOT NULL,
    age        INT         NOT NULL,
    created_at TIMESTAMP   NOT NULL DEFAULT now(),
    type       varchar     not null
);
