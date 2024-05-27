package org.ttaaa.backendhw.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.ttaaa.backendhw.model.dto.GenreDto;
import org.ttaaa.backendhw.model.entity.Genre;
import org.ttaaa.backendhw.service.GenreService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/data/genre")
@AllArgsConstructor
public class GenreController {
    private GenreService genreService;

    @GetMapping("/")
    public List<Genre> all() {
        return genreService.getAllGenres();
    }

    @PostMapping("/new")
    public Genre newGenre(@RequestBody @Valid GenreDto dto) {
        return genreService.saveGenre(dto);
    }

    @GetMapping("/{id}")
    public Genre get(@PathVariable UUID id) {
        return genreService.getGenre(id);
    }

    @PutMapping("/{id}")
    public Genre updateGenre(@RequestBody @Valid GenreDto dto, @PathVariable UUID id) {
        return genreService.updateGenre(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        genreService.deleteGenre(id);
    }
}
