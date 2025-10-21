package com.beautycare.inventory.controller;

import com.beautycare.inventory.controller.dto.InsumoRequestDTO;
import com.beautycare.inventory.controller.dto.InsumoResponseDTO;
import com.beautycare.inventory.service.InsumoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory/insumos") // Ruta base del microservicio
@PreAuthorize("hasRole('ADMIN')") // Seguridad a nivel de clase: Todo aquí es solo para ADMIN
public class InsumoController {

    @Autowired
    private InsumoService insumoService; // Inyecta la interfaz

    /**
     * Endpoint para crear un nuevo insumo.
     * Solo accesible por ADMIN.
     */
    @PostMapping
    public ResponseEntity<InsumoResponseDTO> createInsumo(@Valid @RequestBody InsumoRequestDTO requestDTO) {
        InsumoResponseDTO nuevoInsumo = insumoService.createInsumo(requestDTO);
        // Devuelve un 201 Created
        return new ResponseEntity<>(nuevoInsumo, HttpStatus.CREATED);
    }

    /**
     * Endpoint para obtener todos los insumos.
     * Solo accesible por ADMIN.
     */
    @GetMapping
    public ResponseEntity<List<InsumoResponseDTO>> getAllInsumos() {
        List<InsumoResponseDTO> insumos = insumoService.getAllInsumos();
        return ResponseEntity.ok(insumos); // Devuelve 200 OK
    }

    /**
     * Endpoint para obtener un insumo por su ID.
     * Solo accesible por ADMIN.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InsumoResponseDTO> getInsumoById(@PathVariable Long id) {
        InsumoResponseDTO insumo = insumoService.getInsumoById(id);
        return ResponseEntity.ok(insumo);
    }

    /**
     * Endpoint para actualizar un insumo.
     * Solo accesible por ADMIN.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InsumoResponseDTO> updateInsumo(@PathVariable Long id, @Valid @RequestBody InsumoRequestDTO requestDTO) {
        InsumoResponseDTO insumoActualizado = insumoService.updateInsumo(id, requestDTO);
        return ResponseEntity.ok(insumoActualizado);
    }

    /**
     * Endpoint para eliminar un insumo.
     * Solo accesible por ADMIN.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInsumo(@PathVariable Long id) {
        insumoService.deleteInsumo(id);
        return ResponseEntity.noContent().build(); // Devuelve 204 No Content
    }
}