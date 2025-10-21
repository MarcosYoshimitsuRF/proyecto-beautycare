package com.beautycare.api.controller.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CitaRequestDTO {

    @NotNull(message = "El ID del cliente no puede ser nulo.")
    private Long clienteId;

    @NotNull(message = "El ID del profesional no puede ser nulo.")
    private Long profesionalId;

    @NotNull(message = "El ID del servicio no puede ser nulo.")
    private Long servicioId;

    @NotNull(message = "La fecha y hora de inicio no pueden ser nulas.")
    @Future(message = "La fecha y hora de inicio deben ser en el futuro.")
    private LocalDateTime fechaHoraInicio;

    // Nota: La fechaHoraFin la calculará el servicio basado en la duración del servicio.
    // El estado inicial será 'PENDIENTE' por defecto en el servicio.
}