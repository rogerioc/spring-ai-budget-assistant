package org.rogeriocs.budgetinai.auth.infrastructure.persistence.repository;

import org.rogeriocs.budgetinai.auth.infrastructure.persistence.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
