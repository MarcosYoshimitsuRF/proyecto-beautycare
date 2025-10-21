package com.beautycare.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(name = "password_bcrypt", nullable = false)
    private String passwordBcrypt;

    @Column(nullable = false)
    private boolean enabled = true;

    // Relación Muchos-a-Muchos con Rol
    @ManyToMany(fetch = FetchType.EAGER) // EAGER para cargar roles al cargar usuario
    @JoinTable(
            name = "usuario_rol", // Tabla intermedia
            joinColumns = @JoinColumn(name = "usuario_id"), // Columna de esta entidad
            inverseJoinColumns = @JoinColumn(name = "rol_id") // Columna de la otra entidad
    )
    private Set<Rol> roles;
}