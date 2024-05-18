package org.ttaaa.backendhw.service.dto;

import org.springframework.stereotype.Service;
import org.ttaaa.backendhw.model.dto.AuthorDto;
import org.ttaaa.backendhw.model.entity.Author;

import java.util.UUID;

@Service
public class AuthorDtoService implements DtoService<AuthorDto, Author, UUID> {
    @Override
    public Author dtoToEntity(AuthorDto dto) {
        return dtoToEntity(UUID.randomUUID(), dto);
    }

    @Override
    public Author dtoToEntity(UUID uuid, AuthorDto dto) {
        return new Author(uuid, dto.getFirstName(), dto.getLastName(), dto.getMidName());
    }
}
