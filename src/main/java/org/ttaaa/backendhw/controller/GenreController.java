package org.ttaaa.backendhw.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.ttaaa.backendhw.models.dto.GenreDto;
import org.ttaaa.backendhw.models.entity.Genre;
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

    @PostMapping("/")
    public Genre newGenre(@RequestBody GenreDto newGenre) {
        return genreService.saveGenre(newGenre);
    }

    @GetMapping("/{id}")
    public Genre get(@PathVariable UUID id) {
        return genreService.getGenre(id);
    }

    @PutMapping("/{id}")
    public Genre updateGenre(@RequestBody GenreDto newGenre, @PathVariable UUID id) {
        return genreService.updateGenre(id, newGenre);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        genreService.deleteGenre(id);
    }
}
