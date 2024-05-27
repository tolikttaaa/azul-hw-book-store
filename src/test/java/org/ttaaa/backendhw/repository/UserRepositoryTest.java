package org.ttaaa.backendhw.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.ttaaa.backendhw.config.EnablePostgreSqlContainer;
import org.ttaaa.backendhw.model.entity.User;
import org.ttaaa.backendhw.model.entity.UserRole;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EnablePostgreSqlContainer
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private static final String TEST_USER_NAME = "user_test";
    private static final String TEST_ADMIN_NAME = "admin_test";
    private static final String TEST_SYSTEM_NAME = "system_test";
    private static final String TEST_USER_NAME_DELETE = "user_test_delete";
    private static final String TEST_ADMIN_NAME_DELETE = "admin_test_delete";
    private static final String INVALID_USER_NAME = "invalid_username";

    private static final String TEST_PASSWORD = "test_password";

    @BeforeAll
    public void setUp() {
        userRepository.save(new User(UUID.randomUUID(), TEST_USER_NAME, TEST_PASSWORD, UserRole.USER));
        userRepository.save(new User(UUID.randomUUID(), TEST_ADMIN_NAME, TEST_PASSWORD, UserRole.ADMIN));
        userRepository.save(new User(UUID.randomUUID(), TEST_USER_NAME_DELETE, TEST_PASSWORD, UserRole.USER));
        userRepository.save(new User(UUID.randomUUID(), TEST_ADMIN_NAME_DELETE, TEST_PASSWORD, UserRole.ADMIN));
        userRepository.save(new User(UUID.randomUUID(), TEST_SYSTEM_NAME, TEST_PASSWORD, UserRole.SYSTEM));
    }

    @ParameterizedTest
    @ValueSource(strings = {TEST_USER_NAME, TEST_ADMIN_NAME, TEST_USER_NAME_DELETE, TEST_ADMIN_NAME_DELETE, TEST_SYSTEM_NAME})
    public void testFindByUsername_Successful(String username) {
        Optional<User> user = userRepository.findByUsername(username);

        assertThat(user).isPresent();
        assertThat(user.get().getUsername()).isEqualTo(username);
    }

    @Test
    public void testFindByUsername_Unsuccessful() {
        Optional<User> user = userRepository.findByUsername(INVALID_USER_NAME);

        assertThat(user).isNotPresent();
    }

    private Stream<Arguments> usernameRoleUpdateProvider() {
        return Stream.of(
                Arguments.of(TEST_USER_NAME, UserRole.ADMIN),
                Arguments.of(TEST_ADMIN_NAME, UserRole.USER)
        );
    }

    @ParameterizedTest
    @MethodSource("usernameRoleUpdateProvider")
    public void testUpdateRoleByUsername_Successful(String username, UserRole userRole) {
        userRepository.updateRoleByUsername(username, userRole);
        Optional<User> user = userRepository.findByUsername(username);

        assertThat(user).isPresent();
        assertThat(user.get().getRole()).isEqualTo(userRole);
    }

    @Test
    public void testUpdateRoleByUsername_Unsuccessful() {
        userRepository.updateRoleByUsername(TEST_SYSTEM_NAME, UserRole.ADMIN);
        Optional<User> user = userRepository.findByUsername(TEST_SYSTEM_NAME);

        assertThat(user).isPresent();
        assertThat(user.get().getRole()).isEqualTo(UserRole.SYSTEM);
    }

    @ParameterizedTest
    @ValueSource(strings = {TEST_USER_NAME_DELETE, TEST_ADMIN_NAME_DELETE})
    public void testDeleteUserByUsername_Successful(String username) {
        userRepository.deleteByUsername(username);
        Optional<User> user = userRepository.findByUsername(username);

        assertThat(user).isNotPresent();
    }

    @Test
    public void testDeleteUserByUsername_Unsuccessful() {
        userRepository.deleteByUsername(TEST_SYSTEM_NAME);
        Optional<User> user = userRepository.findByUsername(TEST_SYSTEM_NAME);

        assertThat(user).isPresent();
    }

    @AfterAll
    public void tearDown() {
        userRepository.deleteAll();
    }
}
