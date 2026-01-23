package org.spring.security.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePermissionRequest {

    @NotBlank(message = "Permission code is required")
    @Size(max = 100)
    private String code;

    @Size(max = 255)
    private String description;
}

