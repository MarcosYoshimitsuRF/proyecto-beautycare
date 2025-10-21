package com.beautycare.api.repository;

import com.beautycare.api.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    /**
     * Busca citas que se solapen con un intervalo de tiempo dado [inicio, fin)
     * para un cliente específico O un profesional específico.
     * Una cita existente [existInicio, existFin) se solapa si:
     * (existInicio < fin) AND (existFin > inicio)
     *
     * @param inicio      Fecha/hora de inicio del nuevo intervalo.
     * @param fin         Fecha/hora de fin del nuevo intervalo.
     * @param clienteId   ID del cliente.
     * @param profesionalId ID del profesional.
     * @return Lista de citas que se solapan (vacía si no hay ninguna).
     */
    @Query("SELECT c FROM Cita c WHERE " +
            "(c.cliente.id = :clienteId OR c.profesional.id = :profesionalId) AND " +
            "c.fechaHoraInicio < :fin AND c.fechaHoraFin > :inicio")
    List<Cita> findOverlappingCitas(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin,
            @Param("clienteId") Long clienteId,
            @Param("profesionalId") Long profesionalId
    );
}