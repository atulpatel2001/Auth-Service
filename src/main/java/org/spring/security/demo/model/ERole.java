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
@Table(name = "roles")
public class ERole extends BaseEntity{

    private static final long serialVersionUID = 1L;

    @Column(name = "name", length = 100, nullable = false, unique = true)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_permissions", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id"), uniqueConstraints = {
            @UniqueConstraint(name = "uk_role_permission", columnNames = { "role_id", "permission_id" }) })
    private Set<EPermission> permissions = new HashSet<>();
}
