package org.spring.security.demo.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic API request wrapper request data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiRequest <T>{
    @Valid
    private T data;

}
