package org.ttaaa.backendhw.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PaginationResponseDto<E>  {
    private int pageNumber;
    private int pageSize;
    private boolean hasNextPage;
    private final List<E> content;
}
