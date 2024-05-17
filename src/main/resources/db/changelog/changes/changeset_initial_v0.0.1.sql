CREATE TABLE genres (
    id uuid PRIMARY KEY,
    name VARCHAR(256) UNIQUE NOT NULL
);

CREATE TABLE authors (
    id uuid PRIMARY KEY,
    first_name VARCHAR(256) NOT NULL,
    last_name VARCHAR(256) NOT NULL,
    mid_name VARCHAR(256),
    UNIQUE (first_name, last_name, mid_name)
);

CREATE TABLE books (
    id uuid PRIMARY KEY,
    title VARCHAR(256) UNIQUE NOT NULL,
    author_id uuid NOT NULL,
    price DECIMAL(12, 2),
    CONSTRAINT fk_author
        FOREIGN KEY(author_id)
            REFERENCES authors(id)
            ON DELETE CASCADE
);

CREATE TABLE book_to_genre (
    book_id uuid NOT NULL,
    genre_id uuid NOT NULL,
    PRIMARY KEY (book_id, genre_id),
    CONSTRAINT fk_book
        FOREIGN KEY(book_id)
            REFERENCES books(id)
            ON DELETE CASCADE,
    CONSTRAINT fk_genre
        FOREIGN KEY(genre_id)
            REFERENCES genres(id)
            ON DELETE CASCADE
);
