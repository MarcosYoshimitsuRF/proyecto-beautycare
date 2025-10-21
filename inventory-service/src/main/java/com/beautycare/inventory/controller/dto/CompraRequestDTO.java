package com.beautycare.inventory.controller.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CompraRequestDTO {

    // proveedorId can be null according to the schema
    private Long proveedorId;

    @NotNull(message = "La fecha no puede ser nula.")
    private LocalDate fecha;

    @NotNull(message = "El total no puede ser nulo.")
    @DecimalMin(value = "0.01", message = "El total debe ser mayor que cero.")
    private BigDecimal total;

    // Note: We might add details (like List<CompraDetalleDTO>) later
    // if needed for more complex operations, but for basic CRUD this is enough.
}