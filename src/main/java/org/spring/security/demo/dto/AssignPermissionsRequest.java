package org.spring.security.demo.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class AssignPermissionsRequest {

    @NotNull(message = "Role ID is required")
    private Long roleId;

    @NotEmpty(message = "Permission IDs cannot be empty")
    private Set<Long> permissionIds;
}

