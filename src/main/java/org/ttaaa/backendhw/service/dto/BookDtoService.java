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
    public Book dtoToEntity(BookDto bookDto) {
        return dtoToEntity(UUID.randomUUID(), bookDto);
    }

    @Override
    public Book dtoToEntity(UUID id, BookDto bookDto) {
        Author author = authorRepository.getById(bookDto.getAuthorId());
        Set<Genre> genres = genreRepository.getByIds(bookDto.getGenreIds().stream().toList());
        return new Book(id, bookDto.getTitle(), bookDto.getPrice(), author, genres);
    }
}
