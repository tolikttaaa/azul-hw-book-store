package org.ttaaa.backendhw.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.ttaaa.backendhw.config.EnablePostgreSqlContainer;
import org.ttaaa.backendhw.exception.BadRequestException;
import org.ttaaa.backendhw.exception.NotFoundException;
import org.ttaaa.backendhw.model.dto.GenreDto;
import org.ttaaa.backendhw.model.entity.Genre;
import org.ttaaa.backendhw.repository.GenreRepository;
import org.ttaaa.backendhw.service.dto.GenreDtoService;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@EnablePostgreSqlContainer
public class GenreServiceTest {
    @Autowired
    private GenreService genreService;
    @MockBean
    private GenreRepository genreRepository;
    @MockBean
    private GenreDtoService genreDtoService;

    private static final UUID TEST_UUID = UUID.randomUUID();
    private static final UUID ANOTHER_TEST_UUID = UUID.randomUUID();
    private static final String TEST_NAME = "genre_test_name";
    private static final String NEW_TEST_NAME = "new_genre_test_name";

    @Test
    public void getGenre_ThrowGenreNotFoundException() {
        Mockito.when(genreRepository.findById(TEST_UUID)).thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.GenreNotFoundException.class,
                () -> genreService.getGenre(TEST_UUID)
        );
    }

    @Test
    public void getGenre_Successful() {
        Genre genre = new Genre(TEST_UUID, TEST_NAME);
        Mockito.when(genreRepository.findById(TEST_UUID)).thenReturn(Optional.of(genre));

        Genre serviceResponse = assertDoesNotThrow(
                () -> genreService.getGenre(TEST_UUID)
        );

        assertThat(serviceResponse).isEqualTo(genre);
    }

    @Test
    public void saveGenre_ThrowGenreBadRequestException() {
        Genre genre = new Genre(TEST_UUID, TEST_NAME);
        Mockito.when(genreRepository.findByName(TEST_NAME)).thenReturn(Optional.of(genre));

        BadRequestException.GenreBadRequestException exception = assertThrows(
                BadRequestException.GenreBadRequestException.class,
                () -> genreService.saveGenre(new GenreDto(TEST_NAME))
        );

        assertThat(exception.getMessage()).isEqualTo("Genre already exists");
    }

    @Test
    public void saveGenre_Successful() {
        Genre genre = new Genre(TEST_UUID, TEST_NAME);
        Mockito.when(genreRepository.findByName(TEST_NAME)).thenReturn(Optional.empty());
        Mockito.when(genreDtoService.dtoToEntity(new GenreDto(TEST_NAME))).thenReturn(genre);
        Mockito.when(genreRepository.save(genre)).thenReturn(genre);

        Genre serviceResponse = assertDoesNotThrow(
                () -> genreService.saveGenre(new GenreDto(TEST_NAME))
        );

        assertThat(serviceResponse).isEqualTo(genre);
    }

    @Test
    public void updateGenre_ThrowGenreNotFoundException() {
        Mockito.when(genreRepository.findById(TEST_UUID)).thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.GenreNotFoundException.class,
                () -> genreService.updateGenre(TEST_UUID, new GenreDto(NEW_TEST_NAME))
        );
    }

    @Test
    public void updateGenre_ThrowGenreBadRequestException() {
        Genre genre = new Genre(TEST_UUID, TEST_NAME);
        Genre existedGenre = new Genre(ANOTHER_TEST_UUID, NEW_TEST_NAME);

        Mockito.when(genreRepository.findById(TEST_UUID)).thenReturn(Optional.of(genre));
        Mockito.when(genreRepository.findByName(NEW_TEST_NAME)).thenReturn(Optional.of(existedGenre));

        BadRequestException.GenreBadRequestException exception = assertThrows(
                BadRequestException.GenreBadRequestException.class,
                () -> genreService.updateGenre(TEST_UUID, new GenreDto(NEW_TEST_NAME))
        );

        assertThat(exception.getMessage()).isEqualTo("Genre with such params already exists");
    }

    @Test
    public void updateGenre_sameValue_Successful() {
        Genre genre = new Genre(TEST_UUID, TEST_NAME);

        Mockito.when(genreDtoService.dtoToEntity(TEST_UUID, new GenreDto(TEST_NAME))).thenReturn(genre);
        Mockito.when(genreRepository.findById(TEST_UUID)).thenReturn(Optional.of(genre));
        Mockito.when(genreRepository.findByName(TEST_NAME)).thenReturn(Optional.of(genre));
        Mockito.when(genreRepository.save(genre)).thenReturn(genre);

        Genre serviceResponse = assertDoesNotThrow(
                () -> genreService.updateGenre(TEST_UUID, new GenreDto(TEST_NAME))
        );

        assertThat(serviceResponse).isEqualTo(genre);
    }

    @Test
    public void updateGenre_newValue_Successful() {
        Genre genre = new Genre(TEST_UUID, TEST_NAME);
        Genre newGenre = new Genre(TEST_UUID, NEW_TEST_NAME);

        Mockito.when(genreDtoService.dtoToEntity(TEST_UUID, new GenreDto(NEW_TEST_NAME))).thenReturn(newGenre);
        Mockito.when(genreRepository.findById(TEST_UUID)).thenReturn(Optional.of(genre));
        Mockito.when(genreRepository.findByName(NEW_TEST_NAME)).thenReturn(Optional.empty());
        Mockito.when(genreRepository.save(newGenre)).thenReturn(newGenre);

        Genre serviceResponse = assertDoesNotThrow(
                () -> genreService.updateGenre(TEST_UUID, new GenreDto(NEW_TEST_NAME))
        );

        assertThat(serviceResponse).isEqualTo(newGenre);
    }
}
