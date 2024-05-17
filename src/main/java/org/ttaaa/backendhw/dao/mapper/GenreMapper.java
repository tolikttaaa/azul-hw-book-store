package org.ttaaa.backendhw.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.ttaaa.backendhw.models.entity.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class GenreMapper implements RowMapper<Genre> {
    public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
        Genre genre = new Genre();

        genre.setId(UUID.fromString(resultSet.getString("id")));
        genre.setName(resultSet.getString("name"));

        return genre;
    }
}
