package org.spring.security.demo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spring.security.demo.dto.*;

import org.spring.security.demo.service.RolePermissionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/role-permission")
@Validated
@RequiredArgsConstructor
public class RolePermissionController {

    private final RolePermissionService service;



    // CREATE ROLE
    @PreAuthorize("hasAuthority('ROLE_CREATE')")
    @PostMapping("/role/create")
    public ApiResponse<Long> createRole(
            @Valid @RequestBody ApiRequest<CreateRoleRequest> request) {

        return ApiResponse.<Long>builder()
                .success(true)
                .message("Role created successfully")
                .data(service.createRole(request.getData()))
                .build();
    }

    //CREATE PERMISSION
    @PostMapping("/permission/create")
    @PreAuthorize("hasAuthority('PERMISSION_CREATE')")
    public ApiResponse<Long> createPermission(
            @Valid @RequestBody ApiRequest<CreatePermissionRequest> request) {

        return ApiResponse.<Long>builder()
                .success(true)
                .message("Permission created successfully")
                .data(service.createPermission(request.getData()))
                .build();
    }

    // ASSIGN PERMISSIONS
    @PostMapping("/role/assign-permissions")
    @PreAuthorize("hasAuthority('ROLE_ASSIGN_PERMISSIONS')")
    public ApiResponse<RolePermissionResponse> assignPermissions(
            @Valid @RequestBody ApiRequest<AssignPermissionsRequest> request) {

        return ApiResponse.<RolePermissionResponse>builder()
                .success(true)
                .message("Permissions assigned successfully")
                .data(service.assignPermissions(request.getData()))
                .build();
    }


    @PostMapping("/role/get")
    @PreAuthorize("hasAuthority('ROLE_VIEW')")
    public ApiResponse<RolePermissionResponse> getRole(
            @Valid @RequestBody ApiRequest<IdRequest> request) {

        return ApiResponse.<RolePermissionResponse>builder()
                .success(true)
                .message("Role fetched successfully")
                .data(service.getRoleWithPermissions(request.getData().getId()))
                .build();
    }

    @PostMapping("/remove")
    @PreAuthorize("hasAuthority('ROLE_PERMISSION_REMOVE')")
    public ApiResponse<Void> removePermission(
            @Valid @RequestBody ApiRequest<RemoveRolePermissionRequest> request) {

        service
                .removePermissionFromRole(request.getData());

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Permission removed from role successfully")
                .build();
    }
}
