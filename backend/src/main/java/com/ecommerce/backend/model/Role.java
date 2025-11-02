package com.ecommerce.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*; // Using Jakarta Persistence API

/**
 * JPA Entity for managing user roles and mapping them to the database.
 * This entity links the RoleName enum to a database table.
 */
@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Use the RoleName enum for type-safe role representation
    @Enumerated(EnumType.STRING)
    @Column(length = 20, unique = true, nullable = false)
    private RoleName name;

    /**
     * Convenience constructor for creating a Role with just the name.
     * @param name The RoleName enum value.
     */
    public Role(RoleName name) {
        this.name = name;
    }
}
