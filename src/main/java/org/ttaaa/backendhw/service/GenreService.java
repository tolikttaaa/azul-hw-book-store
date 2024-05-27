package org.ttaaa.backendhw.service;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ttaaa.backendhw.exception.BadRequestException;
import org.ttaaa.backendhw.exception.NotFoundException;
import org.ttaaa.backendhw.repository.GenreRepository;
import org.ttaaa.backendhw.model.dto.GenreDto;
import org.ttaaa.backendhw.model.entity.Genre;
import org.ttaaa.backendhw.service.dto.GenreDtoService;

import java.util.*;

@Service
@Transactional
@AllArgsConstructor
public class GenreService {
    private GenreRepository genreRepository;
    private GenreDtoService genreDtoService;

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    public Genre getGenre(UUID id) {
        Optional<Genre> entity = genreRepository.findById(id);
        if (entity.isEmpty()) throw new NotFoundException.GenreNotFoundException(id);
        return entity.get();
    }

    public Set<Genre> getGenresByIds(List<UUID> uuids) {
        return new HashSet<>(genreRepository.findAllByIdIn(uuids));
    }

    public Genre saveGenre(GenreDto dto) {
        Optional<Genre> existing = genreRepository.findByName(dto.getName());
        if (existing.isPresent()) throw new BadRequestException.GenreBadRequestException("Genre already exists", existing.get());

        return genreRepository.save(genreDtoService.dtoToEntity(dto));
    }

    public Genre updateGenre(UUID id, GenreDto dto) {
        getGenre(id);

        Optional<Genre> existing = genreRepository.findByName(dto.getName());
        if (existing.isPresent() && existing.get().getId() != id)
            throw new BadRequestException.GenreBadRequestException("Genre with such params already exists", existing.get());

        return genreRepository.save(genreDtoService.dtoToEntity(id, dto));
    }

    public void deleteGenre(UUID id) {
        genreRepository.deleteById(id);
    }
}
