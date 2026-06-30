package org.spring.security.demo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IdRequest {

    @NotNull(message = "ID is required")
    private Long id;
}
