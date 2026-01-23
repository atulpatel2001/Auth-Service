package org.spring.security.demo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spring.security.demo.dto.ApiRequest;
import org.spring.security.demo.dto.ApiResponse;
import org.spring.security.demo.dto.AssignUserRoleRequest;
import org.spring.security.demo.dto.RemoveUserRoleRequest;
import org.spring.security.demo.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/user")
@Validated
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;



    // ✅ ASSIGN ROLE
    @PostMapping("/assign-role")
    @PreAuthorize("hasAuthority('ASSIGN_ROLE')")
    public ApiResponse<Void> assignRole(
            @Valid @RequestBody ApiRequest<AssignUserRoleRequest> request) {

        userService.assignRoleToUser(request.getData());

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Role assigned to user successfully")
                .build();
    }

    // ✅ REMOVE ROLE
    @PostMapping("/remove-role")
    @PreAuthorize("hasAuthority('REMOVE_ROLE')")
    public ApiResponse<Void> removeRole(
            @Valid @RequestBody ApiRequest<RemoveUserRoleRequest> request) {

        userService.removeRoleFromUser(request.getData());

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Role removed from user successfully")
                .build();
    }
}