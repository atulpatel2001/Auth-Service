package org.spring.security.demo.service;

import org.spring.security.demo.dto.TaskDto;

import java.util.List;

public interface TaskService {

        TaskDto createTask(TaskDto taskDto);
        TaskDto getTaskById(Long id);
        TaskDto updateTask(TaskDto taskDto);
        void deleteTask(Long id);

        List<TaskDto> getAllTasks();
 }
