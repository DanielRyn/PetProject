CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS pet
(
    id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name       VARCHAR(16)    NOT NULL,
    age        INT            NOT NULL,
    created_at TIMESTAMP(100) NOT NULL,
    type       VARCHAR        NOT NULL
);
