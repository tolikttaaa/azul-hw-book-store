package org.ttaaa.backendhw.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.ttaaa.backendhw.dao.mapper.AuthorMapper;
import org.ttaaa.backendhw.exception.BadRequestException;
import org.ttaaa.backendhw.exception.NotFoundException;
import org.ttaaa.backendhw.models.dto.AuthorDto;
import org.ttaaa.backendhw.models.entity.Author;
import org.ttaaa.backendhw.dao.AuthorDAO;

import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

@Component
public class AuthorDAOImpl implements AuthorDAO {
    private final JdbcTemplate jdbcTemplate;

    private final String SQL_FIND_AUTHOR = "SELECT * FROM authors WHERE id = ?";
    private final String SQL_DELETE_AUTHOR = "DELETE FROM authors WHERE id = ?";
    private final String SQL_UPDATE_AUTHOR = "UPDATE authors SET first_name = ?, last_name = ?, mid_name = ? WHERE id = ?";
    private final String SQL_GET_ALL = "SELECT * FROM authors";
    private final String SQL_INSERT_AUTHOR = "INSERT INTO authors (id, first_name, last_name, mid_name) VALUES (?, ?, ?, ?) ON CONFLICT DO NOTHING";

    @Autowired
    public AuthorDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Author insert(Author entity) {
        if (jdbcTemplate.update(SQL_INSERT_AUTHOR, entity.getId(), entity.getFirstName(), entity.getLastName(),
                entity.getMidName()) > 0) return entity;
        else throw new BadRequestException.AuthorBadRequestException(new AuthorDto(entity));
    }

    @Override
    public Author update(UUID id, Author entity) {
        if (jdbcTemplate.update(SQL_UPDATE_AUTHOR, entity.getFirstName(), entity.getLastName(),
                entity.getMidName(), id) > 0) return entity;
        else throw new NotFoundException.AuthorNotFoundException(id);
    }

    @Override
    public Author findById(UUID uuid) {
        return jdbcTemplate.queryForStream(SQL_FIND_AUTHOR, new AuthorMapper(), uuid).findFirst()
                .orElseThrow(() -> new NotFoundException.AuthorNotFoundException(uuid));
    }

    @Override
    public List<Author> findAll() {
        return jdbcTemplate.query(SQL_GET_ALL, new AuthorMapper());
    }

    @Override
    public void deleteById(UUID uuid) {
        if (jdbcTemplate.update(SQL_DELETE_AUTHOR, uuid) == 0)
            throw new NotFoundException.AuthorNotFoundException(uuid);
    }
}
