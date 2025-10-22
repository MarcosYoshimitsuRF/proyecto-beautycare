package com.beautycare.inventory.controller;

import com.beautycare.inventory.controller.dto.InsumoResponseDTO; // Importar DTO
import com.beautycare.inventory.service.InsumoService; // Importar Servicio
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List; // Importar List

@RestController
@RequestMapping("/api/inventory/reportes") // Ruta base para reportes de inventario
@PreAuthorize("hasRole('ADMIN')") // Seguridad a nivel de clase: Solo ADMIN accede
public class ReporteInventarioController {

    @Autowired
    private InsumoService insumoService; // Inyecta el servicio de insumos

    /**
     * Endpoint para obtener la lista de insumos que están bajo el stock mínimo.
     *
     * @return ResponseEntity con la lista de InsumoResponseDTO.
     */
    @GetMapping("/insumos-bajo-stock") // <-- NUEVO ENDPOINT
    public ResponseEntity<List<InsumoResponseDTO>> getInsumosBajoStock() {
        List<InsumoResponseDTO> insumos = insumoService.getInsumosBajoStock();
        return ResponseEntity.ok(insumos); // Devuelve 200 OK con la lista
    }
}