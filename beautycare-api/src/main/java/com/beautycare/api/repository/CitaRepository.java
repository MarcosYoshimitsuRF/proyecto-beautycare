package com.beautycare.api.repository;

import com.beautycare.api.controller.dto.TopServicioDTO; // Importar DTO
import com.beautycare.api.model.Cita;
import org.springframework.data.domain.Pageable; // Importar Pageable
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    // --- Método existente para solapamiento ---
    @Query("SELECT c FROM Cita c WHERE " +
            "(c.cliente.id = :clienteId OR c.profesional.id = :profesionalId) AND " +
            "c.fechaHoraInicio < :fin AND c.fechaHoraFin > :inicio")
    List<Cita> findOverlappingCitas(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin,
            @Param("clienteId") Long clienteId,
            @Param("profesionalId") Long profesionalId
    );

    /**
     * Busca los servicios más realizados en un rango de fechas.
     * Cuenta las citas con estado 'REALIZADA' agrupadas por servicio.
     * Devuelve los resultados mapeados directamente a TopServicioDTO.
     *
     * @param estado El estado de la cita a contar (ej. "REALIZADA").
     * @param desde  Fecha y hora de inicio del rango.
     * @param hasta  Fecha y hora de fin del rango.
     * @param pageable Objeto Pageable que contiene la información de límite (ej. PageRequest.of(0, limit)).
     * @return Lista de TopServicioDTO ordenada por cantidad descendente.
     */
    @Query("SELECT new com.beautycare.api.controller.dto.TopServicioDTO(s.id, s.nombre, COUNT(c)) " +
            "FROM Cita c JOIN c.servicio s " +
            "WHERE c.estado = :estado AND c.fechaHoraInicio >= :desde AND c.fechaHoraInicio < :hasta " +
            "GROUP BY s.id, s.nombre " +
            "ORDER BY COUNT(c) DESC")
    List<TopServicioDTO> findTopServiciosByEstadoAndFecha(
            @Param("estado") String estado,
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta,
            Pageable pageable // <-- Usamos Pageable para limitar resultados
    );
}