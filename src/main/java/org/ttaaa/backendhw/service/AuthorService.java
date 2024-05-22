package org.ttaaa.backendhw.service;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ttaaa.backendhw.exception.BadRequestException;
import org.ttaaa.backendhw.exception.NotFoundException;
import org.ttaaa.backendhw.model.dto.AuthorDto;
import org.ttaaa.backendhw.model.entity.Author;
import org.ttaaa.backendhw.repository.AuthorRepository;
import org.ttaaa.backendhw.service.dto.AuthorDtoService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class AuthorService {
    private AuthorRepository authorRepository;
    private AuthorDtoService authorDtoService;

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Author getAuthor(UUID id) {
        Optional<Author> entity = authorRepository.findById(id);
        if (entity.isEmpty()) throw new NotFoundException.AuthorNotFoundException(id);
        return entity.get();
    }

    public Author saveAuthor(AuthorDto dto) {
        Optional<Author> existing = authorRepository.getByUniqueParams(dto.getFirstName(), dto.getLastName(), dto.getMidName());
        if (existing.isPresent()) throw new BadRequestException.AuthorBadRequestException("Author already exists", existing.get());

        return authorRepository.save(authorDtoService.dtoToEntity(dto));
    }

    public Author updateAuthor(UUID id, AuthorDto dto) {
        getAuthor(id);

        Optional<Author> existing = authorRepository.getByUniqueParams(dto.getFirstName(), dto.getLastName(), dto.getMidName());
        if (existing.isPresent()) throw new BadRequestException.AuthorBadRequestException("Author with such params already exists", existing.get());

        return authorRepository.save(authorDtoService.dtoToEntity(id, dto));
    }

    public void deleteAuthor(UUID id) {
        authorRepository.deleteById(id);
    }
}
