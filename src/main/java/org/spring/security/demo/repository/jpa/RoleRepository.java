package org.spring.security.demo.repository.jpa;

import org.spring.security.demo.model.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<ERole, Long> {

    boolean existsByNameAndIsDeletedFalse(String name);

    Optional<ERole> findByNameAndIsDeletedFalse(String name);

    Optional<ERole> findByIdAndIsDeletedFalse(Long id);

}

