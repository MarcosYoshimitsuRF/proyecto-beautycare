package com.beautycare.api.controller.dto;

import com.beautycare.api.model.Cita;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CitaResponseDTO {

    private Long id;
    private Long clienteId;
    private String clienteNombre;
    private Long profesionalId;
    private String profesionalNombre;
    private Long servicioId;
    private String servicioNombre;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private String estado;

    /**
     * Constructor de conveniencia para mapear
     * una Entidad Cita a este DTO de respuesta.
     */
    public CitaResponseDTO(Cita cita) {
        this.id = cita.getId();
        this.clienteId = cita.getCliente().getId();
        this.clienteNombre = cita.getCliente().getNombre(); // Asume que Cliente tiene getNombre()
        this.profesionalId = cita.getProfesional().getId();
        this.profesionalNombre = cita.getProfesional().getNombre(); // Asume que Profesional tiene getNombre()
        this.servicioId = cita.getServicio().getId();
        this.servicioNombre = cita.getServicio().getNombre(); // Asume que Servicio tiene getNombre()
        this.fechaHoraInicio = cita.getFechaHoraInicio();
        this.fechaHoraFin = cita.getFechaHoraFin();
        this.estado = cita.getEstado();
    }
}