package org.spring.security.demo.service.impl;

import lombok.RequiredArgsConstructor;
import org.spring.security.demo.dto.TaskDto;
import org.spring.security.demo.exception.ResourceNotFoundException;
import org.spring.security.demo.model.Task;
import org.spring.security.demo.repository.jpa.TaskRepoitory;
import org.spring.security.demo.service.MappingService;
import org.spring.security.demo.service.TaskService;
import org.spring.security.demo.util.SecurityContextUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TaskServiceImple implements TaskService {


    private final TaskRepoitory  taskRepoitory;
    private final MappingService objectMapper;

    @Override
    public TaskDto createTask(TaskDto taskDto) {
        Task entity = this.objectMapper.toEntity(taskDto, Task.class);
        entity.setUserId(SecurityContextUtil.getUserId());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setCreatedBy(Objects.requireNonNull(SecurityContextUtil.getUserId()).toString());
        Task save = this.taskRepoitory.save(entity);
        return this.objectMapper.toDTO(save, TaskDto.class);
    }

    @Override
    public TaskDto getTaskById(Long id) {
         Task task = this.taskRepoitory.findByIdAndIsDeletedFalse(id).orElseThrow(() ->
                new ResourceNotFoundException("Task not found"));

        return this.objectMapper.toDTO(task, TaskDto.class);
    }

    @Override
    public TaskDto updateTask(TaskDto taskDto) {
        this.taskRepoitory.findByIdAndIsDeletedFalse(taskDto.getId()).orElseThrow(() ->
                new ResourceNotFoundException("Task not found"));
        Task entity = this.objectMapper.toEntity(taskDto, Task.class);
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy(Objects.requireNonNull(SecurityContextUtil.getUserId()).toString());
        Task save = this.taskRepoitory.save(entity);
        return this.objectMapper.toDTO(save, TaskDto.class);
    }

    @Override
    public void deleteTask(Long id) {
        final Task task = this.taskRepoitory.findByIdAndIsDeletedFalse(id).orElseThrow(() ->
                new ResourceNotFoundException("Task not found"));

        task.setIsDeleted(true);
        task.setUpdatedAt(LocalDateTime.now());
      task.setUpdatedBy(Objects.requireNonNull(SecurityContextUtil.getUserId()).toString());
        this.taskRepoitory.save(task);
    }

    @Override
    public List<TaskDto> getAllTasks() {
        List<Task> tasks = this.taskRepoitory.findAllByIsDeletedFalse();
        return tasks.stream().map(task -> this.objectMapper.toDTO(task, TaskDto.class)).toList();
    }
}
