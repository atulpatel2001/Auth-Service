package org.spring.security.demo.model;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Table(name = "permissions")
public class EPermission extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "code", length = 100, nullable = false, unique = true)
    private String code;

    @Column(name = "description", length = 255)
    private String description;

}
