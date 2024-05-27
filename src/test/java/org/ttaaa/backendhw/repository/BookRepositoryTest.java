package org.ttaaa.backendhw.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.ttaaa.backendhw.config.EnablePostgreSqlContainer;
import org.ttaaa.backendhw.model.entity.Author;
import org.ttaaa.backendhw.model.entity.Book;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EnablePostgreSqlContainer
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;

    private static final String BOOK_TITLE_EXPECTED = "expected_book_title";
    private static final String BOOK_TITLE_UNEXPECTED = "unexpected_book_title";

    private static final String AUTHOR_FIRST_NAME_EXPECTED = "test_first_name";
    private static final String AUTHOR_FIRST_NAME_UNEXPECTED = "unexpected_test_first_name";
    private static final String AUTHOR_LAST_NAME_EXPECTED = "test_last_name";
    private static final String AUTHOR_LAST_NAME_UNEXPECTED = "unexpected_test_last_name";
    private static final String AUTHOR_MID_NAME_EXPECTED = "test_mid_name";
    private static final String AUTHOR_MID_NAME_UNEXPECTED = "unexpected_test_mid_name";

    private Book expectedBook;
    private Author unexpectedAuthor;

    @BeforeAll
    public void setUp() {
        Author expectedAuthor = authorRepository.save(
                new Author(UUID.randomUUID(), AUTHOR_FIRST_NAME_EXPECTED, AUTHOR_LAST_NAME_EXPECTED, AUTHOR_MID_NAME_EXPECTED)
        );
        unexpectedAuthor = authorRepository.save(
                new Author(UUID.randomUUID(), AUTHOR_FIRST_NAME_UNEXPECTED, AUTHOR_LAST_NAME_UNEXPECTED, AUTHOR_MID_NAME_UNEXPECTED)
        );

        expectedBook = bookRepository.save(
                new Book(UUID.randomUUID(), BOOK_TITLE_EXPECTED, 0.0, expectedAuthor, Set.of())
        );
        bookRepository.save(new Book(UUID.randomUUID(), BOOK_TITLE_EXPECTED, 0.0, unexpectedAuthor, Set.of()));
        bookRepository.save(new Book(UUID.randomUUID(), BOOK_TITLE_UNEXPECTED, 0.0, expectedAuthor, Set.of()));
    }

    @Test
    public void testGetByUniqueParams_Successful() {
        Optional<Book> book = bookRepository.getByUniqueParams(expectedBook.getTitle(), expectedBook.getAuthor().getId());

        assertThat(book).isPresent();
        assertThat(book.get()).isEqualTo(expectedBook);
    }


    @Test
    public void testGetByUniqueParams_Unsuccessful() {
        Optional<Book> book = bookRepository.getByUniqueParams(BOOK_TITLE_UNEXPECTED, unexpectedAuthor.getId());

        assertThat(book).isNotPresent();
    }

    @AfterAll
    public void tearDown() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();
    }
}
