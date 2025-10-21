package com.beautycare.api.controller.dto;

import com.beautycare.api.model.Pago;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PagoResponseDTO {

    private Long id;
    private Long citaId;
    private LocalDateTime citaFechaHoraInicio; // Útil para mostrar en el listado
    private String clienteNombre; // Útil para mostrar en el listado
    private BigDecimal monto;
    private String metodo;
    private LocalDateTime fechaHora;

    /**
     * Constructor de conveniencia para mapear
     * una Entidad Pago a este DTO de respuesta.
     */
    public PagoResponseDTO(Pago pago) {
        this.id = pago.getId();
        this.citaId = pago.getCita().getId();
        this.citaFechaHoraInicio = pago.getCita().getFechaHoraInicio();
        this.clienteNombre = pago.getCita().getCliente().getNombre(); // Asume relaciones cargadas
        this.monto = pago.getMonto();
        this.metodo = pago.getMetodo();
        this.fechaHora = pago.getFechaHora();
    }
}