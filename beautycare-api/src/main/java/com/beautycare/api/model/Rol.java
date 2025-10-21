package com.beautycare.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Anotación de Lombok: genera Getters, Setters, toString, equals, hashCode
@NoArgsConstructor // Lombok: genera constructor sin argumentos
@AllArgsConstructor // Lombok: genera constructor con todos los argumentos
@Entity
@Table(name = "rol")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Usa BIGSERIAL de Postgres
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre;
}