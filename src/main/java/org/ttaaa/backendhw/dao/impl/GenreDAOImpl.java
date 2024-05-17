package org.ttaaa.backendhw.dao.impl;

import java.util.List;
import java.util.UUID;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.ttaaa.backendhw.dao.GenreDAO;
import org.ttaaa.backendhw.dao.mapper.GenreMapper;
import org.ttaaa.backendhw.exception.BadRequestException;
import org.ttaaa.backendhw.exception.NotFoundException;
import org.ttaaa.backendhw.models.dto.GenreDto;
import org.ttaaa.backendhw.models.entity.Genre;

@Component
public class GenreDAOImpl implements GenreDAO {
    private final JdbcTemplate jdbcTemplate;

    private final String SQL_FIND_GENRE = "SELECT * FROM genres WHERE id = ?";
    private final String SQL_DELETE_GENRE = "DELETE FROM genres WHERE id = ?";
    private final String SQL_UPDATE_GENRE = "UPDATE genres SET name = ? WHERE id = ?";
    private final String SQL_GET_ALL = "SELECT * FROM genres";
    private final String SQL_INSERT_GENRE =
            "INSERT INTO genres (id, name) VALUES (?, ?) ON CONFLICT DO NOTHING";

    @Autowired
    public GenreDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Genre insert(Genre entity) {
        if (jdbcTemplate.update(SQL_INSERT_GENRE, entity.getId(), entity.getName()) > 0) return entity;
        else throw new BadRequestException.GenreBadRequestException(new GenreDto(entity));
    }

    @Override
    public Genre update(UUID id, Genre entity) {
        if (jdbcTemplate.update(SQL_UPDATE_GENRE, entity.getName(), id) > 0) return entity;
        else throw new NotFoundException.GenreNotFoundException(id);
    }

    @Override
    public Genre findById(UUID uuid) {
        return jdbcTemplate.queryForStream(SQL_FIND_GENRE, new GenreMapper(), uuid).findFirst()
                .orElseThrow(() -> new NotFoundException.GenreNotFoundException(uuid));
    }

    @Override
    public List<Genre> findAll() {
        return jdbcTemplate.query(SQL_GET_ALL, new GenreMapper());
    }

    @Override
    public void deleteById(UUID uuid) {
        if (jdbcTemplate.update(SQL_DELETE_GENRE, uuid) == 0)
            throw new NotFoundException.GenreNotFoundException(uuid);
    }
}
