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
@Table(name = "users")
public class EUser extends BaseEntity{

    private static final long serialVersionUID = 1L;

    @Column(name = "email", length = 150, unique = true)
    private String email;

    @Column(name = "phone_number", length = 15, nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "password", length = 255)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role_mapping", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"), uniqueConstraints = {
            @UniqueConstraint(name = "uk_user_role", columnNames = { "user_id", "role_id" }) })
    @Builder.Default
    private Set<ERole> roles = new HashSet<>();
}
