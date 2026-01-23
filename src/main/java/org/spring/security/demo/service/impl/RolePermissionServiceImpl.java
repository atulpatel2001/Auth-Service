package org.spring.security.demo.service.impl;

import lombok.RequiredArgsConstructor;
import org.spring.security.demo.dto.*;
import org.spring.security.demo.exception.BusinessException;
import org.spring.security.demo.exception.ResourceNotFoundException;
import org.spring.security.demo.model.EPermission;
import org.spring.security.demo.model.ERole;
import org.spring.security.demo.repository.jpa.PermissionRepository;
import org.spring.security.demo.repository.jpa.RoleRepository;
import org.spring.security.demo.service.RolePermissionService;
import org.spring.security.demo.util.SecurityTextUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RolePermissionServiceImpl implements RolePermissionService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;



    @Override
    public Long createRole(CreateRoleRequest request) {

        String roleName = SecurityTextUtil.normalize(request.getName());

        if (roleRepository.existsByNameAndIsDeletedFalse(roleName)) {
            throw new BusinessException("Role already exists", HttpStatus.BAD_REQUEST);
        }

        ERole role = ERole.builder()
                .name(roleName)
                .description(request.getDescription())
                .createdBy("SYSTEM")
                .build();

        return roleRepository.save(role).getId();
    }

    @Override
    public Long createPermission(CreatePermissionRequest request) {

        String permissionCode = SecurityTextUtil.normalize(request.getCode());

        if (permissionRepository.existsByCodeAndIsDeletedFalse(permissionCode)) {
            throw new BusinessException("Permission already exists",HttpStatus.BAD_REQUEST);
        }

        EPermission permission = EPermission.builder()
                .code(permissionCode)
                .description(request.getDescription())
                .createdBy("SYSTEM")
                .build();

        return permissionRepository.save(permission).getId();
    }

    @Override
    public RolePermissionResponse assignPermissions(AssignPermissionsRequest request) {

        ERole role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not found"));

        Set<EPermission> permissions =
                new HashSet<>(permissionRepository.findAllById(
                        request.getPermissionIds()
                ));

        if (permissions.isEmpty()) {
            throw new BusinessException("No valid permissions found",HttpStatus.BAD_REQUEST);
        }

        role.getPermissions().addAll(permissions);
        roleRepository.save(role);

        return mapToResponse(role);
    }

    @Override
    @Transactional(readOnly = true)
    public RolePermissionResponse getRoleWithPermissions(Long roleId) {

        ERole role = roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not found"));

        return mapToResponse(role);
    }

    private RolePermissionResponse mapToResponse(ERole role) {
        return RolePermissionResponse.builder()
                .roleId(role.getId())
                .roleName(role.getName())
                .permissions(
                        role.getPermissions()
                                .stream()
                                .map(EPermission::getCode)
                                .collect(Collectors.toSet())
                )
                .build();
    }


    @Override
    public void removePermissionFromRole(RemoveRolePermissionRequest request) {

        ERole role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not found"));

        EPermission permission = permissionRepository
                .findById(request.getPermissionId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Permission not found"));

        if (!role.getPermissions().contains(permission)) {
            throw new BusinessException(
                    "Permission is not assigned to this role", HttpStatus.BAD_REQUEST);
        }

        role.getPermissions().remove(permission);
        roleRepository.save(role);
    }
}