package org.ttaaa.backendhw.service;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ttaaa.backendhw.repository.GenreRepository;
import org.ttaaa.backendhw.model.dto.GenreDto;
import org.ttaaa.backendhw.model.entity.Genre;
import org.ttaaa.backendhw.service.dto.GenreDtoService;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class GenreService {
    private GenreRepository genreRepository;
    private GenreDtoService genreDtoService;

    public List<Genre> getAllGenres() {
        return genreRepository.getAll();
    }

    public Genre getGenre(UUID id) {
        return genreRepository.getById(id);
    }

    public Genre saveGenre(GenreDto dto) {
        return genreRepository.save(genreDtoService.dtoToEntity(dto));
    }

    public Genre updateGenre(UUID id, GenreDto dto) {
        return genreRepository.update(genreDtoService.dtoToEntity(id, dto));
    }

    public void deleteGenre(UUID id) {
        genreRepository.deleteById(id);
    }
}
