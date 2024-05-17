CREATE INDEX IF NOT EXISTS idx_genre_name ON genres (name);

CREATE INDEX IF NOT EXISTS idx_author_firstName_lastName_midName ON authors (first_name, last_name, mid_name);
