package org.ttaaa.backendhw.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.*;
import org.ttaaa.backendhw.exception.BadRequestException;
import org.ttaaa.backendhw.model.entity.User;
import org.ttaaa.backendhw.model.entity.UserRole;
import org.ttaaa.backendhw.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/data/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/")
    public List<User> all() {
        return userService.getAllUsers();
    }

    @PutMapping("/role/{username}")
    public User updateUserRole(
            @PathVariable @NotEmpty String username,
            @RequestBody @Valid UserRole role) {
        if (role.equals(UserRole.SYSTEM))
            throw new BadRequestException.UserBadRequestException(
                    "UserRole.SYSTEM can not be set up for usual user", role);
        return userService.updateUserRole(username, role);
    }

    @DeleteMapping("/{username}")
    public void delete(@PathVariable String username) {
        userService.deleteUser(username);
    }
}
