CREATE TABLE users (
    id uuid PRIMARY KEY,
    username VARCHAR(256) UNIQUE NOT NULL,
    password VARCHAR(256) NOT NULL,
    role VARCHAR(256)
);
