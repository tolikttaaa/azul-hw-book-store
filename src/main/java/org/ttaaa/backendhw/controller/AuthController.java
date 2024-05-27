package org.ttaaa.backendhw.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ttaaa.backendhw.model.dto.AuthDto;
import org.ttaaa.backendhw.model.dto.LoginResponseDto;
import org.ttaaa.backendhw.model.entity.User;
import org.ttaaa.backendhw.service.AuthService;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public User register(@RequestBody @Valid AuthDto dto) {
        return authService.register(dto);
    }

    @PostMapping("/login")
    public LoginResponseDto authenticate(@RequestBody @Valid AuthDto dto) {
        return authService.authenticate(dto);
    }
}
