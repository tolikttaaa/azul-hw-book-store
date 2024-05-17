package org.ttaaa.backendhw.models.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ttaaa.backendhw.models.BookFilterParam;

@Data
@EqualsAndHashCode(callSuper = true)
public class FilterBookDto extends PageableDto {
    private BookFilterParam bookFilterParam;
}
