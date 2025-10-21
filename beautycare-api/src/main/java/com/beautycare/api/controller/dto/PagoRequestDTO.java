package com.beautycare.api.controller.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PagoRequestDTO {

    @NotNull(message = "El ID de la cita no puede ser nulo.")
    private Long citaId;

    @NotNull(message = "El monto no puede ser nulo.")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor que cero.")
    private BigDecimal monto;

    @NotEmpty(message = "El método de pago no puede estar vacío.")
    @Size(max = 50, message = "El método de pago no puede tener más de 50 caracteres.")
    private String metodo; // Ej. "EFECTIVO", "TARJETA", "YAPE"

    // La fechaHora se establecerá automáticamente en el servicio al crear el pago.
}