package org.spring.security.demo.repository.jpa;
import org.spring.security.demo.model.EPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PermissionRepository extends JpaRepository<EPermission, Long> {

    boolean existsByCodeAndIsDeletedFalse(String code);
}
