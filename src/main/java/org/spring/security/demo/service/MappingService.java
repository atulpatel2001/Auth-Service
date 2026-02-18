package org.spring.security.demo.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Service
@RequiredArgsConstructor
public class MappingService {

    private final ObjectMapper objectMapper;


    @PostConstruct
    public void init() {
        // Register support for LocalDateTime, LocalDate, etc.
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Convert an entity to a DTO
     */
    public <D, E> D toDTO(E entity, Class<D> dtoClass) {
        return objectMapper.convertValue(entity, dtoClass);
    }

    /**
     * Convert a DTO to an entity
     */
    public <E, D> E toEntity(D dto, Class<E> entityClass) {
        return objectMapper.convertValue(dto, entityClass);
    }
}
