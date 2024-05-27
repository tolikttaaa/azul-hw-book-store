package org.ttaaa.backendhw.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.ttaaa.backendhw.exception.BadRequestException;
import org.ttaaa.backendhw.exception.NotFoundException;
import org.ttaaa.backendhw.model.dto.BookDto;
import org.ttaaa.backendhw.model.dto.BookSearchDto;
import org.ttaaa.backendhw.model.dto.PaginationResponseDto;
import org.ttaaa.backendhw.repository.BookFilterRepository;
import org.ttaaa.backendhw.repository.BookRepository;
import org.ttaaa.backendhw.model.entity.Book;
import org.ttaaa.backendhw.service.dto.BookDtoService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookFilterRepository bookFilterRepository;
    private final GenreService genreService;
    private final BookDtoService bookDtoService;
    private final AuthorService authorService;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBook(UUID id) {
        Optional<Book> entity = bookRepository.findById(id);
        if (entity.isEmpty()) throw new NotFoundException.BookNotFoundException(id);
        return entity.get();
    }

    public Book saveBook(BookDto dto) {
        Optional<Book> existing = bookRepository.getByUniqueParams(dto.getTitle(), dto.getAuthorId());
        if (existing.isPresent()) throw new BadRequestException.BookBadRequestException("Book already exists", existing.get());

        return bookRepository.save(bookDtoService.dtoToEntity(dto));
    }

    public Book updateBook(UUID id, BookDto dto) {
        getBook(id);

        Optional<Book> existing = checkBookExistence(dto.getTitle(), dto.getAuthorId());
        if (existing.isPresent() && existing.get().getId() != id)
            throw new BadRequestException.BookBadRequestException("Book with such params already exists", existing.get());

        return bookRepository.save(bookDtoService.dtoToEntity(id, dto));
    }

    private Optional<Book> checkBookExistence(String title, UUID authorId) {
        return bookRepository.getByUniqueParams(title, authorId);
    }

    public void deleteBook(UUID id) {
        bookRepository.deleteById(id);
    }

    public Book updateBookTitle(UUID id, BookDto.TitleDto dto) {
        Book entity = getBook(id);

        Optional<Book> existing = checkBookExistence(dto.getTitle(), entity.getAuthor().getId());
        if (existing.isPresent() && existing.get().getId() != id)
            throw new BadRequestException.BookBadRequestException("Book with such params already exists", existing.get());

        entity.setTitle(dto.getTitle());
        return bookRepository.save(entity);
    }

    public Book updateBookPrice(UUID id, BookDto.PriceDto dto) {
        Book entity = getBook(id);
        entity.setPrice(dto.getPrice());
        return bookRepository.save(entity);
    }

    public Book updateBookAuthor(UUID id, BookDto.AuthorIdDto dto) {
        Book entity = getBook(id);

        Optional<Book> existing = checkBookExistence(entity.getTitle(), dto.getAuthorId());
        if (existing.isPresent() && existing.get().getId() != id)
            throw new BadRequestException.BookBadRequestException("Book with such params already exists", existing.get());

        entity.setAuthor(authorService.getAuthor(dto.getAuthorId()));
        return bookRepository.save(entity);
    }

    public Book updateBookGenres(UUID id, BookDto.GenreIdsDto dto) {
        Book entity = getBook(id);
        entity.setGenres(genreService.getGenresByIds(dto.getGenreIds().stream().toList()));
        return bookRepository.save(entity);
    }

    public PaginationResponseDto<Book> getBookByFilter(BookSearchDto dto, int pageNumber, int pageSize) {
        List<Book> content = bookFilterRepository.getByFilter(
                Optional.ofNullable(dto.getTitle()),
                Optional.ofNullable(dto.getGenreName()),
                Optional.ofNullable(dto.getAuthorFirstName()),
                Optional.ofNullable(dto.getAuthorLastName()),
                Optional.ofNullable(dto.getAuthorMidName()),
                pageNumber * pageSize, pageSize + 1
        );

        return new PaginationResponseDto<>(
                pageNumber, pageSize,
                content.size() > pageSize,
                content.stream().limit(pageSize).toList()
        );
    }
}
