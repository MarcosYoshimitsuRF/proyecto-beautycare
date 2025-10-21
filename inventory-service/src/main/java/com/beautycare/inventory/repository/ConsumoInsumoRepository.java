package com.beautycare.inventory.repository;

import com.beautycare.inventory.model.ConsumoInsumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // Importar List

@Repository
public interface ConsumoInsumoRepository extends JpaRepository<ConsumoInsumo, Long> {

    /**
     * Busca todos los registros de ConsumoInsumo asociados a un servicioId específico.
     * Spring Data JPA generará la consulta automáticamente basada en el nombre del método.
     *
     * @param servicioId El ID del servicio (de beautycare-api).
     * @return Una lista de ConsumoInsumo para ese servicio.
     */
    List<ConsumoInsumo> findByServicioId(Long servicioId); // <-- NUEVO MÉTODO
}