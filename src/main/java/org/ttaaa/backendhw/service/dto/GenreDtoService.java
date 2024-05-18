package org.ttaaa.backendhw.service.dto;

import org.springframework.stereotype.Service;
import org.ttaaa.backendhw.model.dto.GenreDto;
import org.ttaaa.backendhw.model.entity.Genre;

import java.util.UUID;

@Service
public class GenreDtoService implements DtoService<GenreDto, Genre, UUID> {
    @Override
    public Genre dtoToEntity(GenreDto dto) {
        return dtoToEntity(UUID.randomUUID(), dto);
    }

    @Override
    public Genre dtoToEntity(UUID uuid, GenreDto dto) {
        return new Genre(uuid, dto.getName());
    }
}
