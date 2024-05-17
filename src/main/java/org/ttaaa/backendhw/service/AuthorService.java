package org.ttaaa.backendhw.service;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ttaaa.backendhw.exception.NotFoundException;
import org.ttaaa.backendhw.models.dto.AuthorDto;
import org.ttaaa.backendhw.models.entity.Author;
import org.ttaaa.backendhw.dao.AuthorDAO;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class AuthorService {
    private AuthorDAO authorDAO;

    public List<Author> getAllAuthors() {
        return authorDAO.findAll();
    }

    public Author getAuthor(UUID id) {
        return authorDAO.findById(id);
    }

    public Author saveAuthor(AuthorDto authorDto) {
        return authorDAO.insert(authorDto.toEntity());
    }

    public Author updateAuthor(UUID id, AuthorDto newAuthor) {
        return authorDAO.update(id, newAuthor.toEntity(id));
    }

    public void deleteAuthor(UUID id) {
        authorDAO.deleteById(id);
    }
}
