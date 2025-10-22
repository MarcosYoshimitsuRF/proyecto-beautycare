package com.beautycare.api.repository;

import com.beautycare.api.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Importar Query
import org.springframework.data.repository.query.Param; // Importar Param
import org.springframework.stereotype.Repository;

import java.math.BigDecimal; // Importar BigDecimal
import java.time.LocalDateTime; // Importar LocalDateTime

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    /**
     * Calcula la suma total de los montos de los pagos realizados
     * en un rango de fechas específico.
     *
     * @param desde Fecha y hora de inicio del rango (inclusivo).
     * @param hasta Fecha y hora de fin del rango (exclusivo).
     * @return La suma total de los montos, o 0 si no hay pagos en el rango.
     */
    @Query("SELECT COALESCE(SUM(p.monto), 0) FROM Pago p WHERE p.fechaHora >= :desde AND p.fechaHora < :hasta")
    BigDecimal sumMontoBetweenDates(@Param("desde") LocalDateTime desde, @Param("hasta") LocalDateTime hasta); // <-- NUEVO MÉTODO
}