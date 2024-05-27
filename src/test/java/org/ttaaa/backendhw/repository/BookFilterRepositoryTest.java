package org.ttaaa.backendhw.repository;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.ttaaa.backendhw.config.EnablePostgreSqlContainer;
import org.ttaaa.backendhw.model.entity.Author;
import org.ttaaa.backendhw.model.entity.Book;
import org.ttaaa.backendhw.model.entity.Genre;

import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EnablePostgreSqlContainer
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookFilterRepositoryTest {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookFilterRepository bookFilterRepository;

    private static final String BOOK_TITLE_EXPECTED = "expected_book_title";
    private static final String BOOK_TITLE_UNEXPECTED = "unexpected_book_title";

    private static final String AUTHOR_FIRST_NAME_EXPECTED = "test_first_name";
    private static final String AUTHOR_FIRST_NAME_UNEXPECTED = "unexpected_test_first_name";
    private static final String AUTHOR_LAST_NAME_EXPECTED = "test_last_name";
    private static final String AUTHOR_LAST_NAME_UNEXPECTED = "unexpected_test_last_name";
    private static final String AUTHOR_MID_NAME_EXPECTED = "test_mid_name";
    private static final String AUTHOR_MID_NAME_UNEXPECTED = "unexpected_test_mid_name";

    private static final String GENRE_NAME_UNEXPECTED = "test_name_unexpected";
    private static final String GENRE_NAME_EXPECTED_1 = "test_name_expected_1";
    private static final String GENRE_NAME_EXPECTED_2 = "test_name_expected_2";

    private Book expectedBook1;
    private Book expectedBook2;
    private Book expectedBook3;

    @BeforeAll
    public void setUp() {
        Author expectedAuthor = authorRepository.save(
                new Author(UUID.randomUUID(), AUTHOR_FIRST_NAME_EXPECTED, AUTHOR_LAST_NAME_EXPECTED, AUTHOR_MID_NAME_EXPECTED)
        );

        Author expectedFirstNameAuthor = authorRepository.save(
                new Author(UUID.randomUUID(), AUTHOR_FIRST_NAME_EXPECTED, AUTHOR_LAST_NAME_UNEXPECTED, AUTHOR_MID_NAME_UNEXPECTED)
        );

        Author unexpectedAuthor = authorRepository.save(
                new Author(UUID.randomUUID(), AUTHOR_FIRST_NAME_UNEXPECTED, AUTHOR_LAST_NAME_UNEXPECTED, AUTHOR_MID_NAME_UNEXPECTED)
        );

        Genre expectedGenre1 = genreRepository.save(new Genre(UUID.randomUUID(), GENRE_NAME_EXPECTED_1));
        Genre expectedGenre2 = genreRepository.save(new Genre(UUID.randomUUID(), GENRE_NAME_EXPECTED_2));
        Genre unexpectedGenre = genreRepository.save(new Genre(UUID.randomUUID(), GENRE_NAME_UNEXPECTED));

        expectedBook1 = bookRepository.save(
                new Book(UUID.randomUUID(), BOOK_TITLE_EXPECTED, 0.0, expectedAuthor, Set.of(expectedGenre1, expectedGenre2, unexpectedGenre))
        );
        expectedBook2 = bookRepository.save(
                new Book(UUID.randomUUID(), BOOK_TITLE_EXPECTED, 0.0, unexpectedAuthor, Set.of(expectedGenre1, unexpectedGenre))
        );
        expectedBook3 = bookRepository.save(
                new Book(UUID.randomUUID(), BOOK_TITLE_UNEXPECTED, 0.0, expectedFirstNameAuthor, Set.of(expectedGenre1))
        );
    }

    private Stream<Arguments> filterParamsProvider() {
        return Stream.of(
                Arguments.of(
                        Optional.of(BOOK_TITLE_EXPECTED),
                        Optional.of(GENRE_NAME_EXPECTED_1),
                        Optional.of(AUTHOR_FIRST_NAME_EXPECTED),
                        Optional.of(AUTHOR_LAST_NAME_EXPECTED),
                        Optional.of(AUTHOR_MID_NAME_EXPECTED),
                        List.of(expectedBook1)
                ),
                Arguments.of(
                        Optional.of(BOOK_TITLE_EXPECTED),
                        Optional.of(GENRE_NAME_EXPECTED_2),
                        Optional.of(AUTHOR_FIRST_NAME_EXPECTED),
                        Optional.of(AUTHOR_LAST_NAME_EXPECTED),
                        Optional.of(AUTHOR_MID_NAME_EXPECTED),
                        List.of(expectedBook1)
                ),
                Arguments.of(
                        Optional.of(BOOK_TITLE_EXPECTED),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        List.of(expectedBook1, expectedBook2)
                ),
                Arguments.of(
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        List.of(expectedBook1, expectedBook2, expectedBook3)
                ),
                Arguments.of(
                        Optional.of(BOOK_TITLE_UNEXPECTED),
                        Optional.of(GENRE_NAME_UNEXPECTED),
                        Optional.of(AUTHOR_FIRST_NAME_UNEXPECTED),
                        Optional.of(AUTHOR_LAST_NAME_UNEXPECTED),
                        Optional.of(AUTHOR_MID_NAME_UNEXPECTED),
                        List.of()
                ),
                Arguments.of(
                        Optional.empty(),
                        Optional.of(GENRE_NAME_EXPECTED_1),
                        Optional.of(AUTHOR_FIRST_NAME_EXPECTED),
                        Optional.empty(),
                        Optional.empty(),
                        List.of(expectedBook1, expectedBook3)
                ),
                Arguments.of(
                        Optional.empty(),
                        Optional.of(GENRE_NAME_EXPECTED_2),
                        Optional.of(AUTHOR_FIRST_NAME_EXPECTED),
                        Optional.empty(),
                        Optional.empty(),
                        List.of(expectedBook1)
                )
        );
    }

    @ParameterizedTest
    @MethodSource("filterParamsProvider")
    public void testGetByFilter(
            Optional<String> title,
            Optional<String> genreName,
            Optional<String> authorFirstName,
            Optional<String> authorLastName,
            Optional<String> authorMidName,
            List<Book> expectedBooks
    ) {
        List<Book> books = bookFilterRepository.getByFilter(
                title,
                genreName,
                authorFirstName,
                authorLastName,
                authorMidName,
                0,
                100
        );

        assertThat(books).hasSameElementsAs(expectedBooks);
    }

    @Test
    public void testGetByFilterPagination() {
        List<Book> booksPage1 = bookFilterRepository.getByFilter(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                0,
                2
        );
        List<Book> booksPage2 = bookFilterRepository.getByFilter(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                2,
                2
        );

        assertThat(booksPage1).isNotEmpty().hasSize(2);
        assertThat(booksPage2).isNotEmpty().hasSize(1);
        assertThat(booksPage1).doesNotContainAnyElementsOf(booksPage2);

        List<Book> joinBookPages = Stream.concat(booksPage1.stream(), booksPage2.stream()).toList();
        Set<Book> uniqueSet = new HashSet<>(joinBookPages);
        assertThat(joinBookPages).hasSameSizeAs(uniqueSet);
    }

    @AfterAll
    public void tearDown() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        genreRepository.deleteAll();
    }
}
