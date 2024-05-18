package org.ttaaa.backendhw.repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.ttaaa.backendhw.model.entity.Genre;

public interface GenreRepository extends EntityRepository<Genre, UUID> {
    Set<Genre> getByIds(List<UUID> uuids);
}
