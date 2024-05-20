ALTER TABLE books DROP CONSTRAINT books_title_key;

ALTER TABLE books ADD UNIQUE (title, author_id); -- no conflicts due to previous unique constraint
