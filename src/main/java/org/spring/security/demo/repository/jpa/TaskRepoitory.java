package org.spring.security.demo.repository.jpa;

import org.spring.security.demo.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepoitory extends JpaRepository<Task, Long> {

    Optional<Task> findByTitle(String title);
    Optional<Task> findByIdAndIsDeletedFalse(Long id);
    List<Task> findAllByIsDeletedFalse();
}
