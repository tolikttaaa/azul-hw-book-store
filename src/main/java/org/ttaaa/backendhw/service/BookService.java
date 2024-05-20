package org.ttaaa.backendhw.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.ttaaa.backendhw.model.dto.BookDto;
import org.ttaaa.backendhw.model.dto.BookSearchDto;
import org.ttaaa.backendhw.model.dto.PaginationResponseDto;
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
    private BookRepository bookRepository;
    private BookDtoService bookDtoService;

    public List<Book> getAllBooks() {
        return bookRepository.getAll();
    }

    public Book getBook(UUID id) {
        return bookRepository.getById(id);
    }

    public Book saveBook(BookDto dto) {
        return bookRepository.save(bookDtoService.dtoToEntity(dto));
    }

    public Book updateBook(UUID id, BookDto dto) {
        return bookRepository.update(bookDtoService.dtoToEntity(id, dto));
    }

    public void deleteBook(UUID id) {
        bookRepository.deleteById(id);
    }

    public Book updateBookTitle(UUID id, BookDto.TitleDto dto) {
        return bookRepository.updateBookTitle(id, dto.getTitle());
    }

    public Book updateBookPrice(UUID id, BookDto.PriceDto dto) {
        return bookRepository.updateBookPrice(id, dto.getPrice());
    }

    public Book updateBookAuthor(UUID id, BookDto.AuthorIdDto dto) {
        return bookRepository.updateBookAuthor(id, bookDtoService.authorIdDtoToEntity(dto));
    }

    public Book updateBookGenres(UUID id, BookDto.GenreIdsDto dto) {
        return bookRepository.updateBookGenres(id, bookDtoService.genreIdsDtoToEntity(dto));
    }

    public PaginationResponseDto<Book> getBookByFilter(BookSearchDto dto, int pageNumber, int pageSize) {
        List<Book> content = bookRepository.getByFilter(
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
