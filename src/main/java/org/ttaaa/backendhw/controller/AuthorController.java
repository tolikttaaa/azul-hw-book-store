package org.ttaaa.backendhw.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.ttaaa.backendhw.model.dto.AuthorDto;
import org.ttaaa.backendhw.model.entity.Author;
import org.ttaaa.backendhw.service.AuthorService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/data/author")
@AllArgsConstructor
public class AuthorController {
    private AuthorService authorService;

    @GetMapping("/")
    public List<Author> all() {
        return authorService.getAllAuthors();
    }

    @PostMapping("/")
    public Author newAuthor(@RequestBody AuthorDto newAuthor) {
        return authorService.saveAuthor(newAuthor);
    }

    @GetMapping("/{id}")
    public Author get(@PathVariable UUID id) {
        return authorService.getAuthor(id);
    }

    @PutMapping("/{id}")
    public Author updateAuthor(@RequestBody AuthorDto newAuthor, @PathVariable UUID id) {
        return authorService.updateAuthor(id, newAuthor);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        authorService.deleteAuthor(id);
    }
}
