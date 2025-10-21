package com.beautycare.inventory.controller;

import com.beautycare.inventory.controller.dto.CompraRequestDTO;
import com.beautycare.inventory.controller.dto.CompraResponseDTO;
import com.beautycare.inventory.service.CompraService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory/compras") // Ruta base del microservicio
@PreAuthorize("hasRole('ADMIN')") // Seguridad a nivel de clase: Todo aquí es solo para ADMIN
public class CompraController {

    @Autowired
    private CompraService compraService; // Inyecta la interfaz

    /**
     * Endpoint para crear un nuevo registro de compra.
     * Solo accesible por ADMIN.
     */
    @PostMapping
    public ResponseEntity<CompraResponseDTO> createCompra(@Valid @RequestBody CompraRequestDTO requestDTO) {
        CompraResponseDTO nuevaCompra = compraService.createCompra(requestDTO);
        // Devuelve un 201 Created
        return new ResponseEntity<>(nuevaCompra, HttpStatus.CREATED);
    }

    /**
     * Endpoint para obtener todos los registros de compra.
     * Solo accesible por ADMIN.
     */
    @GetMapping
    public ResponseEntity<List<CompraResponseDTO>> getAllCompras() {
        List<CompraResponseDTO> compras = compraService.getAllCompras();
        return ResponseEntity.ok(compras); // Devuelve 200 OK
    }

    /**
     * Endpoint para obtener un registro de compra por su ID.
     * Solo accesible por ADMIN.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CompraResponseDTO> getCompraById(@PathVariable Long id) {
        CompraResponseDTO compra = compraService.getCompraById(id);
        return ResponseEntity.ok(compra);
    }

    /**
     * Endpoint para actualizar un registro de compra.
     * Solo accesible por ADMIN.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CompraResponseDTO> updateCompra(@PathVariable Long id, @Valid @RequestBody CompraRequestDTO requestDTO) {
        CompraResponseDTO compraActualizada = compraService.updateCompra(id, requestDTO);
        return ResponseEntity.ok(compraActualizada);
    }

    /**
     * Endpoint para eliminar un registro de compra.
     * Solo accesible por ADMIN.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompra(@PathVariable Long id) {
        compraService.deleteCompra(id);
        return ResponseEntity.noContent().build(); // Devuelve 204 No Content
    }
}