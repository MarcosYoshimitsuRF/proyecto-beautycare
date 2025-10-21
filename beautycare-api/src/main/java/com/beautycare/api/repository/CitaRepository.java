package com.beautycare.api.repository;

import com.beautycare.api.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    // Más adelante (Fase 4) añadiremos aquí la consulta
    // personalizada para detectar solapamiento de citas.
}