package org.ttaaa.backendhw.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ttaaa.backendhw.exception.BadRequestException;
import org.ttaaa.backendhw.model.dto.AuthDto;
import org.ttaaa.backendhw.model.entity.User;
import org.ttaaa.backendhw.model.entity.UserRole;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class AuthService {
    private final UserService userService;

    public User register(AuthDto dto) {
        Optional<User> user = userService.getUserByUsername(dto.getUsername());
        if (user.isPresent()) throw new BadRequestException.UserBadRequestException("User already exists", dto.getUsername());

        return userService.createUser(dto.getUsername(), dto.getPassword(), UserRole.USER);
    }

    public User authenticate(AuthDto dto) {
        return userService.getUserByUsername(dto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
