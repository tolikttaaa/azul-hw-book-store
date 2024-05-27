package org.ttaaa.backendhw.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.ttaaa.backendhw.config.EnablePostgreSqlContainer;
import org.ttaaa.backendhw.exception.BadRequestException;
import org.ttaaa.backendhw.exception.NotFoundException;
import org.ttaaa.backendhw.model.dto.BookDto;
import org.ttaaa.backendhw.model.entity.Author;
import org.ttaaa.backendhw.model.entity.Book;
import org.ttaaa.backendhw.model.entity.Genre;
import org.ttaaa.backendhw.repository.BookRepository;
import org.ttaaa.backendhw.service.dto.BookDtoService;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@EnablePostgreSqlContainer
public class BookServiceTest {
    @Autowired
    private BookService bookService;
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private GenreService genreService;
    @MockBean
    private BookDtoService bookDtoService;
    @MockBean
    private AuthorService authorService;

    private static final UUID TEST_UUID = UUID.randomUUID();
    private static final UUID ANOTHER_TEST_UUID = UUID.randomUUID();
    private static final String TEST_TITLE = "book_test_title";
    private static final String NEW_TEST_TITLE = "new_book_test_title";
    private static final Double TEST_PRICE = 0.0;
    private static final Double NEW_TEST_PRICE = 199.99;
    private static final Author TEST_AUTHOR = new Author(
            UUID.randomUUID(),
            "test_first_name",
            "test_last_name",
            null
    );
    private static final Author NEW_TEST_AUTHOR = new Author(
            UUID.randomUUID(),
            "new test_first_name",
            "new_test_last_name",
            "new_test_mid_name"
    );
    private static final Set<Genre> TEST_GENRES =
            Set.of(new Genre(UUID.randomUUID(), "genre_test_name"));
    private static final Set<Genre> NEW_TEST_GENRES =
            Set.of(new Genre(UUID.randomUUID(), "new_genre_test_name"));

    @Test
    public void getBook_ThrowBookNotFoundException() {
        Mockito.when(bookRepository.findById(TEST_UUID)).thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.BookNotFoundException.class,
                () -> bookService.getBook(TEST_UUID)
        );
    }

    @Test
    public void getBook_Successful() {
        Book book = new Book(TEST_UUID, TEST_TITLE, TEST_PRICE, TEST_AUTHOR, TEST_GENRES);
        Mockito.when(bookRepository.findById(TEST_UUID)).thenReturn(Optional.of(book));

        Book serviceResponse = assertDoesNotThrow(
                () -> bookService.getBook(TEST_UUID)
        );

        assertThat(serviceResponse).isEqualTo(book);
    }

    @Test
    public void saveBook_ThrowBookBadRequestException() {
        Book book = new Book(TEST_UUID, TEST_TITLE, TEST_PRICE, TEST_AUTHOR, TEST_GENRES);
        Mockito.when(bookRepository.getByUniqueParams(TEST_TITLE, TEST_AUTHOR.getId())).thenReturn(Optional.of(book));

        BadRequestException.BookBadRequestException exception = assertThrows(
                BadRequestException.BookBadRequestException.class,
                () -> bookService.saveBook(
                        new BookDto(TEST_TITLE,
                                TEST_PRICE,
                                TEST_AUTHOR.getId(),
                                TEST_GENRES.stream().map(Genre::getId).collect(Collectors.toSet())
                        )
                )
        );

        assertThat(exception.getMessage()).isEqualTo("Book already exists");
    }

    @Test
    public void saveBook_Successful() {
        Book book = new Book(TEST_UUID, TEST_TITLE, TEST_PRICE, TEST_AUTHOR, TEST_GENRES);
        Mockito.when(bookRepository.getByUniqueParams(TEST_TITLE, TEST_AUTHOR.getId())).thenReturn(Optional.empty());
        Mockito.when(bookDtoService.dtoToEntity(
                new BookDto(TEST_TITLE,
                        TEST_PRICE,
                        TEST_AUTHOR.getId(),
                        TEST_GENRES.stream().map(Genre::getId).collect(Collectors.toSet())
                )
        )).thenReturn(book);
        Mockito.when(bookRepository.save(book)).thenReturn(book);

        Book serviceResponse = assertDoesNotThrow(
                () -> bookService.saveBook(
                        new BookDto(TEST_TITLE,
                                TEST_PRICE,
                                TEST_AUTHOR.getId(),
                                TEST_GENRES.stream().map(Genre::getId).collect(Collectors.toSet())
                        )
                )
        );

        assertThat(serviceResponse).isEqualTo(book);
    }

    @Test
    public void updateBook_ThrowBookNotFoundException() {
        Mockito.when(bookRepository.findById(TEST_UUID)).thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.BookNotFoundException.class,
                () -> bookService.updateBook(
                        TEST_UUID,
                        new BookDto(NEW_TEST_TITLE,
                                NEW_TEST_PRICE,
                                NEW_TEST_AUTHOR.getId(),
                                NEW_TEST_GENRES.stream().map(Genre::getId).collect(Collectors.toSet())
                        )
                )
        );
    }

    @Test
    public void updateBook_ThrowBookBadRequestException() {
        Book book = new Book(TEST_UUID, TEST_TITLE, TEST_PRICE, TEST_AUTHOR, TEST_GENRES);
        Book existedBook = new Book(ANOTHER_TEST_UUID, NEW_TEST_TITLE, NEW_TEST_PRICE, NEW_TEST_AUTHOR, NEW_TEST_GENRES);

        Mockito.when(bookRepository.findById(TEST_UUID)).thenReturn(Optional.of(book));
        Mockito.when(bookRepository.getByUniqueParams(NEW_TEST_TITLE, NEW_TEST_AUTHOR.getId()))
                .thenReturn(Optional.of(existedBook));

        BadRequestException.BookBadRequestException exception = assertThrows(
                BadRequestException.BookBadRequestException.class,
                () -> bookService.updateBook(
                        TEST_UUID,
                        new BookDto(NEW_TEST_TITLE,
                                NEW_TEST_PRICE,
                                NEW_TEST_AUTHOR.getId(),
                                NEW_TEST_GENRES.stream().map(Genre::getId).collect(Collectors.toSet())
                        )
                )
        );

        assertThat(exception.getMessage()).isEqualTo("Book with such params already exists");
    }

    @Test
    public void updateBook_sameValue_Successful() {
        Book book = new Book(TEST_UUID, TEST_TITLE, TEST_PRICE, TEST_AUTHOR, TEST_GENRES);

        Mockito.when(bookDtoService.dtoToEntity(
                TEST_UUID,
                new BookDto(NEW_TEST_TITLE,
                        NEW_TEST_PRICE,
                        NEW_TEST_AUTHOR.getId(),
                        NEW_TEST_GENRES.stream().map(Genre::getId).collect(Collectors.toSet())
                )
        )).thenReturn(book);
        Mockito.when(bookRepository.findById(TEST_UUID)).thenReturn(Optional.of(book));
        Mockito.when(bookRepository.getByUniqueParams(TEST_TITLE, TEST_AUTHOR.getId())).thenReturn(Optional.of(book));
        Mockito.when(bookRepository.save(book)).thenReturn(book);

        Book serviceResponse = assertDoesNotThrow(
                () -> bookService.updateBook(
                        TEST_UUID,
                        new BookDto(NEW_TEST_TITLE,
                                NEW_TEST_PRICE,
                                NEW_TEST_AUTHOR.getId(),
                                NEW_TEST_GENRES.stream().map(Genre::getId).collect(Collectors.toSet())
                        )
                )
        );

        assertThat(serviceResponse).isEqualTo(book);
    }

    @Test
    public void updateBook_newValue_Successful() {
        Book book = new Book(TEST_UUID, TEST_TITLE, TEST_PRICE, TEST_AUTHOR, TEST_GENRES);
        Book newBook = new Book(TEST_UUID, NEW_TEST_TITLE, NEW_TEST_PRICE, NEW_TEST_AUTHOR, NEW_TEST_GENRES);

        Mockito.when(bookDtoService.dtoToEntity(
                TEST_UUID,
                new BookDto(NEW_TEST_TITLE,
                        NEW_TEST_PRICE,
                        NEW_TEST_AUTHOR.getId(),
                        NEW_TEST_GENRES.stream().map(Genre::getId).collect(Collectors.toSet())
                )
        )).thenReturn(newBook);
        Mockito.when(bookRepository.findById(TEST_UUID)).thenReturn(Optional.of(book));
        Mockito.when(bookRepository.getByUniqueParams(TEST_TITLE, TEST_AUTHOR.getId())).thenReturn(Optional.empty());
        Mockito.when(bookRepository.save(newBook)).thenReturn(newBook);

        Book serviceResponse = assertDoesNotThrow(
                () -> bookService.updateBook(
                        TEST_UUID,
                        new BookDto(NEW_TEST_TITLE,
                                NEW_TEST_PRICE,
                                NEW_TEST_AUTHOR.getId(),
                                NEW_TEST_GENRES.stream().map(Genre::getId).collect(Collectors.toSet())
                        )
                )
        );

        assertThat(serviceResponse).isEqualTo(newBook);
    }

    @Test
    public void updateBookTitle_ThrowBookNotFoundException() {
        Mockito.when(bookRepository.findById(TEST_UUID)).thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.BookNotFoundException.class,
                () -> bookService.updateBookTitle(TEST_UUID, new BookDto.TitleDto(NEW_TEST_TITLE))
        );
    }

    @Test
    public void updateBookTitle_ThrowBookBadRequestException() {
        Book book = new Book(TEST_UUID, TEST_TITLE, TEST_PRICE, TEST_AUTHOR, TEST_GENRES);
        Book existedBook = new Book(ANOTHER_TEST_UUID, NEW_TEST_TITLE, NEW_TEST_PRICE, TEST_AUTHOR, NEW_TEST_GENRES);

        Mockito.when(bookRepository.findById(TEST_UUID)).thenReturn(Optional.of(book));
        Mockito.when(bookRepository.getByUniqueParams(NEW_TEST_TITLE, TEST_AUTHOR.getId()))
                .thenReturn(Optional.of(existedBook));

        BadRequestException.BookBadRequestException exception = assertThrows(
                BadRequestException.BookBadRequestException.class,
                () -> bookService.updateBookTitle(TEST_UUID, new BookDto.TitleDto(NEW_TEST_TITLE))
        );

        assertThat(exception.getMessage()).isEqualTo("Book with such params already exists");
    }

    @Test
    public void updateBookTitle_sameValue_Successful() {
        Book book = new Book(TEST_UUID, TEST_TITLE, TEST_PRICE, TEST_AUTHOR, TEST_GENRES);

        Mockito.when(bookRepository.findById(TEST_UUID)).thenReturn(Optional.of(book));
        Mockito.when(bookRepository.getByUniqueParams(TEST_TITLE, TEST_AUTHOR.getId())).thenReturn(Optional.of(book));
        Mockito.when(bookRepository.save(book)).thenReturn(book);

        Book serviceResponse = assertDoesNotThrow(
                () -> bookService.updateBookTitle(TEST_UUID, new BookDto.TitleDto(TEST_TITLE))
        );

        assertThat(serviceResponse).isEqualTo(book);
    }

    @Test
    public void updateBookTitle_newValue_Successful() {
        Book book = new Book(TEST_UUID, TEST_TITLE, TEST_PRICE, TEST_AUTHOR, TEST_GENRES);
        Book newBook = new Book(TEST_UUID, NEW_TEST_TITLE, TEST_PRICE, TEST_AUTHOR, TEST_GENRES);

        Mockito.when(bookRepository.findById(TEST_UUID)).thenReturn(Optional.of(book));
        Mockito.when(bookRepository.getByUniqueParams(TEST_TITLE, TEST_AUTHOR.getId())).thenReturn(Optional.empty());
        Mockito.when(bookRepository.save(newBook)).thenReturn(newBook);

        Book serviceResponse = assertDoesNotThrow(
                () -> bookService.updateBookTitle(TEST_UUID, new BookDto.TitleDto(NEW_TEST_TITLE))
        );

        assertThat(serviceResponse).isEqualTo(newBook);
    }

    @Test
    public void updateBookPrice_ThrowBookNotFoundException() {
        Mockito.when(bookRepository.findById(TEST_UUID)).thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.BookNotFoundException.class,
                () -> bookService.updateBookPrice(TEST_UUID, new BookDto.PriceDto(TEST_PRICE))
        );
    }

    @Test
    public void updateBookPrice_sameValue_Successful() {
        Book book = new Book(TEST_UUID, TEST_TITLE, TEST_PRICE, TEST_AUTHOR, TEST_GENRES);

        Mockito.when(bookRepository.findById(TEST_UUID)).thenReturn(Optional.of(book));
        Mockito.when(bookRepository.save(book)).thenReturn(book);

        Book serviceResponse = assertDoesNotThrow(
                () -> bookService.updateBookPrice(TEST_UUID, new BookDto.PriceDto(TEST_PRICE))
        );

        assertThat(serviceResponse).isEqualTo(book);
    }

    @Test
    public void updateBookPrice_newValue_Successful() {
        Book book = new Book(TEST_UUID, TEST_TITLE, TEST_PRICE, TEST_AUTHOR, TEST_GENRES);
        Book newBook = new Book(TEST_UUID, TEST_TITLE, NEW_TEST_PRICE, TEST_AUTHOR, TEST_GENRES);

        Mockito.when(bookRepository.findById(TEST_UUID)).thenReturn(Optional.of(book));
        Mockito.when(bookRepository.save(newBook)).thenReturn(newBook);

        Book serviceResponse = assertDoesNotThrow(
                () -> bookService.updateBookPrice(TEST_UUID, new BookDto.PriceDto(NEW_TEST_PRICE))
        );

        assertThat(serviceResponse).isEqualTo(newBook);
    }

    @Test
    public void updateBookAuthor_ThrowBookNotFoundException() {
        Mockito.when(bookRepository.findById(TEST_UUID)).thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.BookNotFoundException.class,
                () -> bookService.updateBookAuthor(TEST_UUID, new BookDto.AuthorIdDto(NEW_TEST_AUTHOR.getId()))
        );
    }

    @Test
    public void updateBookAuthor_ThrowBookBadRequestException() {
        Book book = new Book(TEST_UUID, TEST_TITLE, TEST_PRICE, TEST_AUTHOR, TEST_GENRES);
        Book existedBook = new Book(ANOTHER_TEST_UUID, TEST_TITLE, NEW_TEST_PRICE, NEW_TEST_AUTHOR, NEW_TEST_GENRES);

        Mockito.when(bookRepository.findById(TEST_UUID)).thenReturn(Optional.of(book));
        Mockito.when(bookRepository.getByUniqueParams(TEST_TITLE, NEW_TEST_AUTHOR.getId()))
                .thenReturn(Optional.of(existedBook));

        BadRequestException.BookBadRequestException exception = assertThrows(
                BadRequestException.BookBadRequestException.class,
                () -> bookService.updateBookAuthor(TEST_UUID, new BookDto.AuthorIdDto(NEW_TEST_AUTHOR.getId()))
        );

        assertThat(exception.getMessage()).isEqualTo("Book with such params already exists");
    }

    @Test
    public void updateBookAuthor_sameValue_Successful() {
        Book book = new Book(TEST_UUID, TEST_TITLE, TEST_PRICE, TEST_AUTHOR, TEST_GENRES);

        Mockito.when(bookRepository.findById(TEST_UUID)).thenReturn(Optional.of(book));
        Mockito.when(bookRepository.getByUniqueParams(TEST_TITLE, TEST_AUTHOR.getId())).thenReturn(Optional.of(book));
        Mockito.when(bookRepository.save(book)).thenReturn(book);
        Mockito.when(authorService.getAuthor(TEST_AUTHOR.getId())).thenReturn(TEST_AUTHOR);

        Book serviceResponse = assertDoesNotThrow(
                () -> bookService.updateBookAuthor(TEST_UUID, new BookDto.AuthorIdDto(TEST_AUTHOR.getId()))
        );

        assertThat(serviceResponse).isEqualTo(book);
    }

    @Test
    public void updateBookAuthor_newValue_Successful() {
        Book book = new Book(TEST_UUID, TEST_TITLE, TEST_PRICE, TEST_AUTHOR, TEST_GENRES);
        Book newBook = new Book(TEST_UUID, TEST_TITLE, TEST_PRICE, NEW_TEST_AUTHOR, TEST_GENRES);

        Mockito.when(bookRepository.findById(TEST_UUID)).thenReturn(Optional.of(book));
        Mockito.when(bookRepository.getByUniqueParams(TEST_TITLE, TEST_AUTHOR.getId())).thenReturn(Optional.empty());
        Mockito.when(bookRepository.save(newBook)).thenReturn(newBook);
        Mockito.when(authorService.getAuthor(NEW_TEST_AUTHOR.getId())).thenReturn(NEW_TEST_AUTHOR);

        Book serviceResponse = assertDoesNotThrow(
                () -> bookService.updateBookAuthor(TEST_UUID, new BookDto.AuthorIdDto(NEW_TEST_AUTHOR.getId()))
        );

        assertThat(serviceResponse).isEqualTo(newBook);
    }

    @Test
    public void updateBookGenres_ThrowBookNotFoundException() {
        Mockito.when(bookRepository.findById(TEST_UUID)).thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.BookNotFoundException.class,
                () -> bookService.updateBookGenres(
                        TEST_UUID,
                        new BookDto.GenreIdsDto(TEST_GENRES.stream().map(Genre::getId).collect(Collectors.toSet()))
                )
        );
    }

    @Test
    public void updateBookGenres_sameValue_Successful() {
        Book book = new Book(TEST_UUID, TEST_TITLE, TEST_PRICE, TEST_AUTHOR, TEST_GENRES);

        Mockito.when(bookRepository.findById(TEST_UUID)).thenReturn(Optional.of(book));
        Mockito.when(bookRepository.save(book)).thenReturn(book);
        Mockito.when(genreService.getGenresByIds(TEST_GENRES.stream().map(Genre::getId).toList())).thenReturn(TEST_GENRES);

        Book serviceResponse = assertDoesNotThrow(
                () -> bookService.updateBookGenres(
                        TEST_UUID,
                        new BookDto.GenreIdsDto(TEST_GENRES.stream().map(Genre::getId).collect(Collectors.toSet()))
                )
        );

        assertThat(serviceResponse).isEqualTo(book);
    }

    @Test
    public void updateBookGenres_newValue_Successful() {
        Book book = new Book(TEST_UUID, TEST_TITLE, TEST_PRICE, TEST_AUTHOR, TEST_GENRES);
        Book newBook = new Book(TEST_UUID, TEST_TITLE, TEST_PRICE, TEST_AUTHOR, NEW_TEST_GENRES);

        Mockito.when(bookRepository.findById(TEST_UUID)).thenReturn(Optional.of(book));
        Mockito.when(bookRepository.save(newBook)).thenReturn(newBook);
        Mockito.when(genreService.getGenresByIds(NEW_TEST_GENRES.stream().map(Genre::getId).toList())).thenReturn(NEW_TEST_GENRES);

        Book serviceResponse = assertDoesNotThrow(
                () -> bookService.updateBookGenres(
                        TEST_UUID,
                        new BookDto.GenreIdsDto(NEW_TEST_GENRES.stream().map(Genre::getId).collect(Collectors.toSet()))
                )
        );

        assertThat(serviceResponse).isEqualTo(newBook);
    }
}
