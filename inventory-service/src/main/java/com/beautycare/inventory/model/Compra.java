package com.beautycare.inventory.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "compra")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación Muchos-a-Uno con Proveedor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id") // Es nullable, coincide con el SQL
    private Proveedor proveedor;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;
}