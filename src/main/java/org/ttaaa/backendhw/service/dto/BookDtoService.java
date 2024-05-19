package org.ttaaa.backendhw.service.dto;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.ttaaa.backendhw.model.dto.BookDto;
import org.ttaaa.backendhw.model.entity.Author;
import org.ttaaa.backendhw.model.entity.Book;
import org.ttaaa.backendhw.model.entity.Genre;
import org.ttaaa.backendhw.repository.AuthorRepository;
import org.ttaaa.backendhw.repository.GenreRepository;

import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BookDtoService implements DtoService<BookDto, Book, UUID> {
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    @Override
    public Book dtoToEntity(BookDto dto) {
        return dtoToEntity(UUID.randomUUID(), dto);
    }

    @Override
    public Book dtoToEntity(UUID id, BookDto dto) {
        Author author = authorRepository.getById(dto.getAuthorId());
        Set<Genre> genres = genreRepository.getByIds(dto.getGenreIds().stream().toList());
        return new Book(id, dto.getTitle(), dto.getPrice(), author, genres);
    }

    public Author authorIdDtoToEntity(BookDto.AuthorIdDto dto) {
        return authorRepository.getById(dto.getAuthorId());
    }

    public Set<Genre> genreIdsDtoToEntity(BookDto.GenreIdsDto dto) {
        return genreRepository.getByIds(dto.getGenreIds().stream().toList());
    }
}
