package org.spring.security.demo.repository.jpa;

import org.spring.security.demo.model.EUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<EUser, Long> {

    boolean existsByEmailAndIsDeletedFalse(String email);

    boolean existsByPhoneNumberAndIsDeletedFalse(String phoneNumber);

    Optional<EUser> findByIdAndIsDeletedFalse(Long id);

    Optional<EUser> findByEmailOrPhoneNumberAndIsDeletedFalse(String email, String phoneNumber);
}
