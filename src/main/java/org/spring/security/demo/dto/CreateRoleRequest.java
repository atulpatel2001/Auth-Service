package org.spring.security.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRoleRequest {

    @NotBlank(message = "Role name is required")
    @Size(max = 100)
    private String name;

    @Size(max = 255)
    private String description;
}

