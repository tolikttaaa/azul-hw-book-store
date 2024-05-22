package org.ttaaa.backendhw.service.dto;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.ttaaa.backendhw.model.dto.BookDto;
import org.ttaaa.backendhw.model.entity.Author;
import org.ttaaa.backendhw.model.entity.Book;
import org.ttaaa.backendhw.model.entity.Genre;
import org.ttaaa.backendhw.service.AuthorService;
import org.ttaaa.backendhw.service.GenreService;

import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BookDtoService implements DtoService<BookDto, Book, UUID> {
    private final AuthorService authorService;
    private final GenreService genreService;

    @Override
    public Book dtoToEntity(BookDto dto) {
        return dtoToEntity(UUID.randomUUID(), dto);
    }

    @Override
    public Book dtoToEntity(UUID id, BookDto dto) {
        Author author = authorService.getAuthor(dto.getAuthorId());
        Set<Genre> genres = genreService.getGenresByIds(dto.getGenreIds().stream().toList());
        return new Book(id, dto.getTitle(), dto.getPrice(), author, genres);
    }
}
