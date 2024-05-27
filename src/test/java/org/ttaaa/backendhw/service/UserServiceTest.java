package org.ttaaa.backendhw.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.ttaaa.backendhw.config.EnablePostgreSqlContainer;
import org.ttaaa.backendhw.exception.BadRequestException;
import org.ttaaa.backendhw.model.entity.User;
import org.ttaaa.backendhw.model.entity.UserRole;
import org.ttaaa.backendhw.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@EnablePostgreSqlContainer
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;

    private static final String TEST_USERNAME = "test_username";
    private static final String TEST_PASSWORD = "test_password";

    @Test
    public void updateUserRole_ThrowUserNotFoundException() {
        Mockito.when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userService.updateUserRole(TEST_USERNAME, UserRole.USER)
        );

        assertThat(exception.getMessage()).isEqualTo("User not found");
    }

    @Test
    public void updateUserRole_ThrowUserBadRequestException() {
        User user = new User(UUID.randomUUID(), TEST_USERNAME, TEST_PASSWORD, UserRole.USER);
        Mockito.when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(user));

        BadRequestException.UserBadRequestException exception = assertThrows(
                BadRequestException.UserBadRequestException.class,
                () -> userService.updateUserRole(TEST_USERNAME, UserRole.SYSTEM)
        );

        assertThat(exception.getMessage()).isEqualTo("UserRole.SYSTEM can not be set up");
    }

    @Test
    public void updateUserRole_Successful() {
        User user = new User(UUID.randomUUID(), TEST_USERNAME, TEST_PASSWORD, UserRole.USER);
        Mockito.when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(user));

        User updatedUser = assertDoesNotThrow(() -> userService.updateUserRole(TEST_USERNAME, UserRole.ADMIN));

        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(TEST_USERNAME);
        Mockito.verify(userRepository, Mockito.times(1)).updateRoleByUsername(TEST_USERNAME, UserRole.ADMIN);

        assertThat(updatedUser.getRole()).isEqualTo(UserRole.ADMIN);
    }

    @Test
    public void createUser_ThrowBadRequestException() {
        User user = new User(UUID.randomUUID(), TEST_USERNAME, TEST_PASSWORD, UserRole.USER);
        Mockito.when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(user));

        BadRequestException.UserBadRequestException exception = assertThrows(
                BadRequestException.UserBadRequestException.class,
                () -> userService.createUser(TEST_USERNAME, TEST_PASSWORD)
        );

        assertThat(exception.getMessage()).isEqualTo("User already exists");
    }

    @Test
    public void createUser_Successful() {
        Mockito.when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> userService.createUser(TEST_USERNAME, TEST_PASSWORD));

        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(TEST_USERNAME);
        Mockito.verify(userRepository, Mockito.times(1)).save(any());
    }

    @ParameterizedTest
    @EnumSource(UserRole.class)
    public void createUserIfNotExist_Successful(UserRole userRole) {
        Mockito.when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> userService.createUserWithRoleIfNotExist(TEST_USERNAME, TEST_PASSWORD, userRole));

        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(TEST_USERNAME);
        Mockito.verify(userRepository, Mockito.times(1)).save(any());
    }

    @ParameterizedTest
    @EnumSource(UserRole.class)
    public void createUserIfNotExist_Unsuccessful(UserRole userRole) {
        User user = new User(UUID.randomUUID(), TEST_USERNAME, TEST_PASSWORD, UserRole.USER);
        Mockito.when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.createUserWithRoleIfNotExist(TEST_USERNAME, TEST_PASSWORD, userRole));

        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(TEST_USERNAME);
    }
}
