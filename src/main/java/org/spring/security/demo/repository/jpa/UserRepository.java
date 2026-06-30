package org.spring.security.demo.repository.jpa;

import org.spring.security.demo.model.EUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<EUser, Long> {

    boolean existsByEmailAndIsDeletedFalse(String email);

    boolean existsByPhoneNumberAndIsDeletedFalse(String phoneNumber);

    Optional<EUser> findByIdAndIsDeletedFalse(Long id);

    Optional<EUser> findByEmailOrPhoneNumberAndIsDeletedFalse(String email, String phoneNumber);

    Optional<EUser> findByEmailAndIsDeletedFalse(String email);

    @Query("SELECT u FROM EUser u LEFT JOIN FETCH u.roles r LEFT JOIN FETCH r.permissions WHERE u.email = :email AND u.isDeleted = false")
    Optional<EUser> findByEmailWithRolesAndIsDeletedFalse(String email);
}
