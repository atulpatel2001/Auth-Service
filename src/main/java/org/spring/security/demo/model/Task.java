package org.spring.security.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Table(name = "tasks")
public class Task extends BaseEntity{

    @Serial
    private static final long serialVersionUID = 1L;

    private String title;
    private String description;
    private Boolean isBillable = Boolean.FALSE;

    private LocalDateTime startTime;
    private LocalDateTime endTime;


    private Long userId;


}
