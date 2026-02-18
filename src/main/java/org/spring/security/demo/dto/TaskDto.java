package org.spring.security.demo.dto;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    private Long id;
    private String title;
    private String description;
    private Boolean isBillable;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
