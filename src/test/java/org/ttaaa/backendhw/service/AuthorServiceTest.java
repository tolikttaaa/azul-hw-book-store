package org.ttaaa.backendhw.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.ttaaa.backendhw.config.EnablePostgreSqlContainer;
import org.ttaaa.backendhw.exception.BadRequestException;
import org.ttaaa.backendhw.exception.NotFoundException;
import org.ttaaa.backendhw.model.dto.AuthorDto;
import org.ttaaa.backendhw.model.entity.Author;
import org.ttaaa.backendhw.repository.AuthorRepository;
import org.ttaaa.backendhw.service.dto.AuthorDtoService;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@EnablePostgreSqlContainer
public class AuthorServiceTest {
    @Autowired
    private AuthorService authorService;
    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private AuthorDtoService authorDtoService;

    private static final UUID TEST_UUID = UUID.randomUUID();
    private static final UUID ANOTHER_TEST_UUID = UUID.randomUUID();
    private static final String TEST_FIRST_NAME = "test_author_first_name";
    private static final String TEST_LAST_NAME = "test_author_last_name";
    private static final String TEST_MID_NAME = null;
    private static final String NEW_TEST_FIRST_NAME = "new_test_author_first_name";
    private static final String NEW_TEST_LAST_NAME = "new_test_author_last_name";
    private static final String NEW_TEST_MID_NAME = "new_test_author_mid_name";

    @Test
    public void getAuthor_ThrowAuthorNotFoundException() {
        Mockito.when(authorRepository.findById(TEST_UUID)).thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.AuthorNotFoundException.class,
                () -> authorService.getAuthor(TEST_UUID)
        );
    }

    @Test
    public void getAuthor_Successful() {
        Author author = new Author(TEST_UUID, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_MID_NAME);
        Mockito.when(authorRepository.findById(TEST_UUID)).thenReturn(Optional.of(author));

        Author serviceResponse = assertDoesNotThrow(
                () -> authorService.getAuthor(TEST_UUID)
        );

        assertThat(serviceResponse).isEqualTo(author);
    }

    @Test
    public void saveAuthor_ThrowAuthorBadRequestException() {
        Author author = new Author(TEST_UUID, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_MID_NAME);
        Mockito.when(authorRepository.getByUniqueParams(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_MID_NAME))
                .thenReturn(Optional.of(author));

        BadRequestException.AuthorBadRequestException exception = assertThrows(
                BadRequestException.AuthorBadRequestException.class,
                () -> authorService.saveAuthor(new AuthorDto(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_MID_NAME))
        );

        assertThat(exception.getMessage()).isEqualTo("Author already exists");
    }

    @Test
    public void saveAuthor_Successful() {
        Author author = new Author(TEST_UUID, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_MID_NAME);
        Mockito.when(authorRepository.getByUniqueParams(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_MID_NAME))
                .thenReturn(Optional.empty());
        Mockito.when(authorDtoService.dtoToEntity(new AuthorDto(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_MID_NAME)))
                .thenReturn(author);
        Mockito.when(authorRepository.save(author)).thenReturn(author);

        Author serviceResponse = assertDoesNotThrow(
                () -> authorService.saveAuthor(new AuthorDto(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_MID_NAME))
        );

        assertThat(serviceResponse).isEqualTo(author);
    }

    @Test
    public void updateAuthor_ThrowAuthorNotFoundException() {
        Mockito.when(authorRepository.findById(TEST_UUID)).thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.AuthorNotFoundException.class,
                () -> authorService.updateAuthor(
                        TEST_UUID,
                        new AuthorDto(NEW_TEST_FIRST_NAME, NEW_TEST_LAST_NAME, NEW_TEST_MID_NAME)
                )
        );
    }

    @Test
    public void updateAuthor_ThrowAuthorBadRequestException() {
        Author author = new Author(TEST_UUID, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_MID_NAME);
        Author existedAuthor = new Author(ANOTHER_TEST_UUID, NEW_TEST_FIRST_NAME, NEW_TEST_LAST_NAME, NEW_TEST_MID_NAME);

        Mockito.when(authorRepository.findById(TEST_UUID)).thenReturn(Optional.of(author));
        Mockito.when(authorRepository.getByUniqueParams(NEW_TEST_FIRST_NAME, NEW_TEST_LAST_NAME, NEW_TEST_MID_NAME))
                .thenReturn(Optional.of(existedAuthor));

        BadRequestException.AuthorBadRequestException exception = assertThrows(
                BadRequestException.AuthorBadRequestException.class,
                () -> authorService.updateAuthor(
                        TEST_UUID,
                        new AuthorDto(NEW_TEST_FIRST_NAME, NEW_TEST_LAST_NAME, NEW_TEST_MID_NAME)
                )
        );

        assertThat(exception.getMessage()).isEqualTo("Author with such params already exists");
    }

    @Test
    public void updateAuthor_sameValue_Successful() {
        Author author = new Author(TEST_UUID, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_MID_NAME);

        Mockito.when(authorDtoService.dtoToEntity(TEST_UUID, new AuthorDto(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_MID_NAME)))
                .thenReturn(author);
        Mockito.when(authorRepository.findById(TEST_UUID)).thenReturn(Optional.of(author));
        Mockito.when(authorRepository.getByUniqueParams(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_MID_NAME))
                .thenReturn(Optional.of(author));
        Mockito.when(authorRepository.save(author)).thenReturn(author);

        Author serviceResponse = assertDoesNotThrow(
                () -> authorService.updateAuthor(
                        TEST_UUID,
                        new AuthorDto(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_MID_NAME)
                )
        );

        assertThat(serviceResponse).isEqualTo(author);
    }

    @Test
    public void updateAuthor_newValue_Successful() {
        Author author = new Author(TEST_UUID, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_MID_NAME);
        Author newAuthor = new Author(TEST_UUID, NEW_TEST_FIRST_NAME, NEW_TEST_LAST_NAME, NEW_TEST_MID_NAME);

        Mockito.when(authorDtoService.dtoToEntity(
                TEST_UUID,
                new AuthorDto(NEW_TEST_FIRST_NAME, NEW_TEST_LAST_NAME, NEW_TEST_MID_NAME))
        ).thenReturn(newAuthor);
        Mockito.when(authorRepository.findById(TEST_UUID)).thenReturn(Optional.of(author));
        Mockito.when(authorRepository.getByUniqueParams(NEW_TEST_FIRST_NAME, NEW_TEST_LAST_NAME, NEW_TEST_MID_NAME))
                .thenReturn(Optional.empty());
        Mockito.when(authorRepository.save(newAuthor)).thenReturn(newAuthor);

        Author serviceResponse = assertDoesNotThrow(
                () -> authorService.updateAuthor(
                        TEST_UUID,
                        new AuthorDto(NEW_TEST_FIRST_NAME, NEW_TEST_LAST_NAME, NEW_TEST_MID_NAME)
                )
        );

        assertThat(serviceResponse).isEqualTo(newAuthor);
    }
}
