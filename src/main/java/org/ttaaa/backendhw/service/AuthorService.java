package org.ttaaa.backendhw.service;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ttaaa.backendhw.model.dto.AuthorDto;
import org.ttaaa.backendhw.model.entity.Author;
import org.ttaaa.backendhw.repository.AuthorRepository;
import org.ttaaa.backendhw.service.dto.AuthorDtoService;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class AuthorService {
    private AuthorRepository authorRepository;
    private AuthorDtoService authorDtoService;

    public List<Author> getAllAuthors() {
        return authorRepository.getAll();
    }

    public Author getAuthor(UUID id) {
        return authorRepository.getById(id);
    }

    public Author saveAuthor(AuthorDto dto) {
        return authorRepository.save(authorDtoService.dtoToEntity(dto));
    }

    public Author updateAuthor(UUID id, AuthorDto dto) {
        return authorRepository.update(authorDtoService.dtoToEntity(id, dto));
    }

    public void deleteAuthor(UUID id) {
        authorRepository.deleteById(id);
    }
}
