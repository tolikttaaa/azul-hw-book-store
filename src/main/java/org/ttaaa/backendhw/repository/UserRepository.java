package org.ttaaa.backendhw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.ttaaa.backendhw.model.entity.User;
import org.ttaaa.backendhw.model.entity.UserRole;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    @Modifying
    @Query("DELETE FROM User u WHERE u.username = :username AND u.role != \"SYSTEM\"")
    void deleteByUsername(@Param("username") String username);

    @Modifying
    @Query("UPDATE User u SET u.role = :role WHERE u.username = :username AND u.role != \"SYSTEM\"")
    void updateRoleByUsername(@Param("username") String username, @Param("role") UserRole role);
}
