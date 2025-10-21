package com.beautycare.inventory.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "consumo_insumo")
public class ConsumoInsumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ID del servicio en la BD 'beautycare_db'.
     * Es numérico y sin Foreign Key, tal como se definió en la documentación [cite: 309-311].
     */
    @Column(name = "servicio_id", nullable = false)
    private Long servicioId;

    // Relación Muchos-a-Uno con Insumo (de esta BD)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insumo_id", nullable = false)
    private Insumo insumo;

    @Column(name = "cantidad_por_servicio", nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidadPorServicio;
}