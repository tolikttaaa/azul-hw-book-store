package org.ttaaa.backendhw.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.ttaaa.backendhw.models.entity.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthorMapper implements RowMapper<Author> {
    public Author mapRow(ResultSet resultSet, int i) throws SQLException {
        Author author = new Author();

        author.setId(UUID.fromString(resultSet.getString("id")));
        author.setFirstName(resultSet.getString("first_name"));
        author.setLastName(resultSet.getString("last_name"));
        author.setMidName(resultSet.getString("mid_name"));

        return author;
    }
}
