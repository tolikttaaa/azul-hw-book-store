package org.ttaaa.backendhw.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ttaaa.backendhw.exception.BadRequestException;
import org.ttaaa.backendhw.model.entity.User;
import org.ttaaa.backendhw.model.entity.UserRole;
import org.ttaaa.backendhw.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(String username, String password) {
        Optional<User> user = getUserByUsername(username);
        if (user.isPresent()) throw new BadRequestException.UserBadRequestException("User already exists", username);

        return userRepository.save(new User(UUID.randomUUID(), username, passwordEncoder.encode(password), UserRole.USER));
    }

    public void createUserWithRoleIfNotExist(String username, String password, UserRole userRole) {
        Optional<User> user = getUserByUsername(username);
        if (user.isEmpty()) userRepository.save(new User(UUID.randomUUID(), username, passwordEncoder.encode(password), userRole));
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User updateUserRole(String username, UserRole role) {
        if (role == UserRole.SYSTEM)
            throw new BadRequestException.UserBadRequestException("UserRole.SYSTEM can not be set up", role);

        Optional<User> user = getUserByUsername(username);
        if (user.isEmpty()) throw new UsernameNotFoundException("User not found");
        userRepository.updateRoleByUsername(username, role);
        User updatedUser = user.get();
        updatedUser.setRole(role);
        return updatedUser;
    }

    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
