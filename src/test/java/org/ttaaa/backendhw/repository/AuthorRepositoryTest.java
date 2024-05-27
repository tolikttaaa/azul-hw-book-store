package org.ttaaa.backendhw.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.ttaaa.backendhw.config.EnablePostgreSqlContainer;
import org.ttaaa.backendhw.model.entity.Author;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EnablePostgreSqlContainer
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthorRepositoryTest {
    @Autowired
    private AuthorRepository authorRepository;

    private static final String AUTHOR_FIRST_NAME_EXPECTED = "test_first_name";
    private static final String AUTHOR_FIRST_NAME_UNEXPECTED = "unexpected_test_first_name";
    private static final String AUTHOR_LAST_NAME_EXPECTED = "test_last_name";
    private static final String AUTHOR_LAST_NAME_UNEXPECTED = "unexpected_test_last_name";
    private static final String AUTHOR_MID_NAME_EXPECTED = "test_mid_name";
    private static final String AUTHOR_MID_NAME_UNEXPECTED = "unexpected_test_mid_name";

    private Author authorValid;

    @BeforeAll
    public void setUp() {
        authorValid = authorRepository.save(new Author(UUID.randomUUID(), AUTHOR_FIRST_NAME_EXPECTED, AUTHOR_LAST_NAME_EXPECTED, AUTHOR_MID_NAME_EXPECTED));
        authorRepository.save(new Author(UUID.randomUUID(), AUTHOR_FIRST_NAME_UNEXPECTED, AUTHOR_LAST_NAME_EXPECTED, AUTHOR_MID_NAME_EXPECTED));
        authorRepository.save(new Author(UUID.randomUUID(), AUTHOR_FIRST_NAME_EXPECTED, AUTHOR_LAST_NAME_UNEXPECTED, AUTHOR_MID_NAME_EXPECTED));
        authorRepository.save(new Author(UUID.randomUUID(), AUTHOR_FIRST_NAME_EXPECTED, AUTHOR_LAST_NAME_EXPECTED, AUTHOR_MID_NAME_UNEXPECTED));
    }

    @Test
    public void testGetByUniqueParams_Successful() {
        Optional<Author> author = authorRepository.getByUniqueParams(AUTHOR_FIRST_NAME_EXPECTED, AUTHOR_LAST_NAME_EXPECTED, AUTHOR_MID_NAME_EXPECTED);

        assertThat(author).isPresent();
        assertThat(author.get().getFirstName()).isEqualTo(AUTHOR_FIRST_NAME_EXPECTED);
        assertThat(author.get().getLastName()).isEqualTo(AUTHOR_LAST_NAME_EXPECTED);
        assertThat(author.get().getMidName()).isEqualTo(AUTHOR_MID_NAME_EXPECTED);
        assertThat(author.get().getId()).isEqualTo(authorValid.getId());
    }

    private Stream<Arguments> authorUniqueParamsProvider() {
        return Stream.of(
                Arguments.of(AUTHOR_FIRST_NAME_UNEXPECTED, AUTHOR_LAST_NAME_UNEXPECTED, AUTHOR_FIRST_NAME_UNEXPECTED),
                Arguments.of(AUTHOR_FIRST_NAME_UNEXPECTED, AUTHOR_LAST_NAME_UNEXPECTED, AUTHOR_FIRST_NAME_EXPECTED),
                Arguments.of(AUTHOR_FIRST_NAME_UNEXPECTED, AUTHOR_LAST_NAME_EXPECTED, AUTHOR_FIRST_NAME_UNEXPECTED),
                Arguments.of(AUTHOR_FIRST_NAME_EXPECTED, AUTHOR_LAST_NAME_UNEXPECTED, AUTHOR_FIRST_NAME_UNEXPECTED)
        );
    }

    @ParameterizedTest
    @MethodSource("authorUniqueParamsProvider")
    public void testGetByUniqueParams_Unsuccessful(String firstName, String lastName, String midName) {
        Optional<Author> author = authorRepository.getByUniqueParams(firstName, lastName, midName);

        assertThat(author).isNotPresent();
    }

    @AfterAll
    public void tearDown() {
        authorRepository.deleteAll();
    }
}
