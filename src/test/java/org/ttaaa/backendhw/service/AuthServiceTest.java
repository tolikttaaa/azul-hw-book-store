package org.ttaaa.backendhw.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.ttaaa.backendhw.config.EnablePostgreSqlContainer;
import org.ttaaa.backendhw.exception.BadRequestException;
import org.ttaaa.backendhw.model.dto.AuthDto;
import org.ttaaa.backendhw.model.dto.LoginResponseDto;
import org.ttaaa.backendhw.model.entity.User;
import org.ttaaa.backendhw.model.entity.UserRole;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@EnablePostgreSqlContainer
public class AuthServiceTest {
    @Autowired
    private AuthService authService;
    @MockBean
    private UserService userService;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private AuthenticationManager authenticationManager;

    private static final String TEST_USERNAME = "test_username";
    private static final String TEST_PASSWORD = "test_password";
    private static final String TEST_TOKEN = "test_token";
    private static final Date TEST_DATE = Date.from(Instant.now());

    @Test
    public void testRegister_ThrowUserBadRequestException() {
        User user = new User(UUID.randomUUID(), TEST_USERNAME, TEST_PASSWORD, UserRole.USER);
        Mockito.when(userService.getUserByUsername(TEST_USERNAME)).thenReturn(Optional.of(user));

        BadRequestException.UserBadRequestException exception = assertThrows(
                BadRequestException.UserBadRequestException.class,
                () -> authService.register(new AuthDto(TEST_USERNAME, TEST_PASSWORD))
        );

        assertThat(exception.getMessage()).isEqualTo("User already exists");
    }

    @Test
    public void testRegister_Successful() {
        User user = new User(UUID.randomUUID(), TEST_USERNAME, TEST_PASSWORD, UserRole.USER);

        Mockito.when(userService.getUserByUsername(TEST_USERNAME)).thenReturn(Optional.empty());
        Mockito.when(userService.createUser(TEST_USERNAME, TEST_PASSWORD)).thenReturn(user);

        User createdUser = assertDoesNotThrow(
                () -> authService.register(new AuthDto(TEST_USERNAME, TEST_PASSWORD))
        );

        assertThat(createdUser).isEqualTo(user);
    }

    @Test
    public void testAuthenticate_ThrowUserNotFoundException() {
        Mockito.doReturn(null).when(authenticationManager).authenticate(any());
        Mockito.when(userService.getUserByUsername(TEST_USERNAME)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> authService.authenticate(new AuthDto(TEST_USERNAME, TEST_PASSWORD))
        );

        assertThat(exception.getMessage()).isEqualTo("User not found");
    }

    @Test
    public void testAuthenticate_Successful() {
        User user = new User(UUID.randomUUID(), TEST_USERNAME, TEST_PASSWORD, UserRole.USER);

        Mockito.doReturn(null).when(authenticationManager).authenticate(any());
        Mockito.when(userService.getUserByUsername(TEST_USERNAME)).thenReturn(Optional.of(user));
        Mockito.when(jwtService.generateToken(user)).thenReturn(TEST_TOKEN);
        Mockito.when(jwtService.extractExpiration(TEST_TOKEN)).thenReturn(TEST_DATE);

        LoginResponseDto loginResponseDto = assertDoesNotThrow(
                () -> authService.authenticate(new AuthDto(TEST_USERNAME, TEST_PASSWORD))
        );

        assertThat(loginResponseDto.getExpiresAt()).isEqualTo(TEST_DATE);
        assertThat(loginResponseDto.getToken()).isEqualTo(TEST_TOKEN);
    }
}
