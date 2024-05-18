package org.ttaaa.backendhw.service.dto;

public interface DtoService<D, E, ID> {
    E dtoToEntity(D dto);
    E dtoToEntity(ID id, D dto);
}
