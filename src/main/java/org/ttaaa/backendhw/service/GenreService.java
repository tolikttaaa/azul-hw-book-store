package org.ttaaa.backendhw.service;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ttaaa.backendhw.dao.GenreDAO;
import org.ttaaa.backendhw.exception.NotFoundException;
import org.ttaaa.backendhw.models.dto.GenreDto;
import org.ttaaa.backendhw.models.entity.Genre;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class GenreService {
    private GenreDAO genreDAO;

    public List<Genre> getAllGenres() {
        return genreDAO.findAll();
    }

    public Genre getGenre(UUID id) {
        return genreDAO.findById(id);
    }

    public Genre saveGenre(GenreDto genreDto) {
        return genreDAO.insert(genreDto.toEntity());
    }

    public Genre updateGenre(UUID id, GenreDto newGenre) {
        return genreDAO.update(id, newGenre.toEntity(id));
    }

    public void deleteGenre(UUID id) {
        genreDAO.deleteById(id);
    }
}
