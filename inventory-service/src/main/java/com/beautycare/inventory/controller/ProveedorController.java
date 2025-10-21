package com.beautycare.inventory.controller;

import com.beautycare.inventory.controller.dto.ProveedorRequestDTO;
import com.beautycare.inventory.controller.dto.ProveedorResponseDTO;
import com.beautycare.inventory.service.ProveedorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory/proveedores") // Ruta base del microservicio
@PreAuthorize("hasRole('ADMIN')") // Seguridad a nivel de clase: Todo aquí es solo para ADMIN
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService; // Inyecta la interfaz

    /**
     * Endpoint para crear un nuevo proveedor.
     * Solo accesible por ADMIN.
     */
    @PostMapping
    public ResponseEntity<ProveedorResponseDTO> createProveedor(@Valid @RequestBody ProveedorRequestDTO requestDTO) {
        ProveedorResponseDTO nuevoProveedor = proveedorService.createProveedor(requestDTO);
        // Devuelve un 201 Created
        return new ResponseEntity<>(nuevoProveedor, HttpStatus.CREATED);
    }

    /**
     * Endpoint para obtener todos los proveedores.
     * Solo accesible por ADMIN.
     */
    @GetMapping
    public ResponseEntity<List<ProveedorResponseDTO>> getAllProveedores() {
        List<ProveedorResponseDTO> proveedores = proveedorService.getAllProveedores();
        return ResponseEntity.ok(proveedores); // Devuelve 200 OK
    }

    /**
     * Endpoint para obtener un proveedor por su ID.
     * Solo accesible por ADMIN.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProveedorResponseDTO> getProveedorById(@PathVariable Long id) {
        ProveedorResponseDTO proveedor = proveedorService.getProveedorById(id);
        return ResponseEntity.ok(proveedor);
    }

    /**
     * Endpoint para actualizar un proveedor.
     * Solo accesible por ADMIN.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProveedorResponseDTO> updateProveedor(@PathVariable Long id, @Valid @RequestBody ProveedorRequestDTO requestDTO) {
        ProveedorResponseDTO proveedorActualizado = proveedorService.updateProveedor(id, requestDTO);
        return ResponseEntity.ok(proveedorActualizado);
    }

    /**
     * Endpoint para eliminar un proveedor.
     * Solo accesible por ADMIN.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProveedor(@PathVariable Long id) {
        proveedorService.deleteProveedor(id);
        return ResponseEntity.noContent().build(); // Devuelve 204 No Content
    }
}