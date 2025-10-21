package com.beautycare.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pago")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación Muchos-a-Uno con Cita
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cita_id", nullable = false)
    private Cita cita;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(length = 50)
    private String metodo;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;
}