package org.spring.security.demo.controller;


import lombok.RequiredArgsConstructor;
import org.spring.security.demo.dto.*;
import org.spring.security.demo.service.TaskService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Validated
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;


    @PostMapping("/add")
    public ApiResponse<TaskDto> addTask(@RequestBody ApiRequest<TaskDto> request) {
        return ApiResponse.<TaskDto>builder()
                .success(true)
                .message("Task added successfully")
                .data(taskService.createTask(request.getData()))
                .build();
    }

    @PostMapping("/update")
    public ApiResponse<TaskDto> updateTask(@RequestBody ApiRequest<TaskDto> request) {
        return ApiResponse.<TaskDto>builder()
                .success(true)
                .message("Task updated successfully")
                .data(taskService.updateTask(request.getData()))
                .build();
    }

    @PostMapping("/get")
    public ApiResponse<TaskDto> getTaskById(@RequestBody ApiRequest<IdRequest> request) {
        return ApiResponse.<TaskDto>builder()
                .success(true)
                .message("Task retrieved successfully")
                .data(taskService.getTaskById(request.getData().getId()))
                .build();

    }

    @PostMapping("/delete")
    public ApiResponse<Void> deleteTask(@RequestBody ApiRequest<IdRequest> request) {
        taskService.deleteTask(request.getData().getId());
        return ApiResponse.<Void>builder()
                .success(true)
                .message("Task deleted successfully")
                .build();
        }



    @PostMapping("/list")
    public ApiResponse<List<TaskDto>> listTasks() {
        return ApiResponse.<List<TaskDto>>builder()
                .success(true)
                .message("Tasks retrieved successfully")
                .data(taskService.getAllTasks())
                .build();
    }





}
