package org.ttaaa.backendhw.service;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ttaaa.backendhw.exception.BadRequestException;
import org.ttaaa.backendhw.model.dto.AuthDto;
import org.ttaaa.backendhw.model.dto.LoginResponseDto;
import org.ttaaa.backendhw.model.entity.User;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public User register(AuthDto dto) {
        Optional<User> user = userService.getUserByUsername(dto.getUsername());
        if (user.isPresent()) throw new BadRequestException.UserBadRequestException("User already exists", dto.getUsername());

        return userService.createUser(dto.getUsername(), dto.getPassword());
    }

    public LoginResponseDto authenticate(AuthDto dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        User authenticatedUser = userService.getUserByUsername(dto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String jwtToken = jwtService.generateToken(authenticatedUser);
        return new LoginResponseDto(jwtToken, jwtService.extractExpiration(jwtToken));
    }
}
