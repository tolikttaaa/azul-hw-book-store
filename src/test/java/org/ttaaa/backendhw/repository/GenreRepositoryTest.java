package org.ttaaa.backendhw.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.ttaaa.backendhw.config.EnablePostgreSqlContainer;
import org.ttaaa.backendhw.model.entity.Genre;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EnablePostgreSqlContainer
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GenreRepositoryTest {
    @Autowired
    private GenreRepository genreRepository;

    private static final String GENRE_NAME_EXPECTED_1 = "test_name_expected_1";
    private static final String GENRE_NAME_EXPECTED_2 = "test_name_expected_2";
    private static final String GENRE_NAME_EXPECTED_3 = "test_name_expected_3";
    private static final String GENRE_NAME_EXPECTED_4 = "test_name_expected_4";
    private static final UUID GENRE_UUID_UNEXPECTED = UUID.randomUUID();
    private static final String GENRE_NAME_UNEXPECTED = "test_name_unexpected";

    private Genre validGenre1;
    private Genre validGenre2;

    @BeforeAll
    public void setUp() {
        validGenre1 = genreRepository.save(new Genre(UUID.randomUUID(), GENRE_NAME_EXPECTED_1));
        validGenre2 = genreRepository.save(new Genre(UUID.randomUUID(), GENRE_NAME_EXPECTED_2));

        genreRepository.save(new Genre(UUID.randomUUID(), GENRE_NAME_EXPECTED_3));
        genreRepository.save(new Genre(UUID.randomUUID(), GENRE_NAME_EXPECTED_4));
    }

    @Test
    public void testFindAllByIdIn_Successful() {
        List<Genre> genres = genreRepository.findAllByIdIn(List.of(validGenre1.getId(), validGenre2.getId()));

        assertThat(genres)
                .isNotEmpty()
                .hasSameElementsAs(List.of(validGenre1, validGenre2));
    }

    @Test
    public void testFindAllByIds_Unsuccessful() {
        List<Genre> genres = genreRepository.findAllByIdIn(List.of(GENRE_UUID_UNEXPECTED));

        assertThat(genres).isEmpty();
    }

    @Test
    public void testFindByName_Successful() {
        Optional<Genre> genre = genreRepository.findByName(GENRE_NAME_EXPECTED_1);

        assertThat(genre).isPresent();
        assertThat(genre.get().getName()).isEqualTo(GENRE_NAME_EXPECTED_1);
    }

    @Test
    public void testFindByName_Unsuccessful() {
        Optional<Genre> genre = genreRepository.findByName(GENRE_NAME_UNEXPECTED);

        assertThat(genre).isNotPresent();
    }

    @AfterAll
    public void tearDown() {
        genreRepository.deleteAll();
    }
}
