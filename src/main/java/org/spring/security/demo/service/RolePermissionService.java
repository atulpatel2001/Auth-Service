package org.spring.security.demo.service;

import org.spring.security.demo.dto.*;

public interface RolePermissionService {

    Long createRole(CreateRoleRequest request);

    Long createPermission(CreatePermissionRequest request);

    RolePermissionResponse assignPermissions(AssignPermissionsRequest request);

    RolePermissionResponse getRoleWithPermissions(Long roleId);

    void removePermissionFromRole(RemoveRolePermissionRequest request);

}

