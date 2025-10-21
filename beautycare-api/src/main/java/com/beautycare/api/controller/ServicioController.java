package com.beautycare.api.controller;

import com.beautycare.api.controller.dto.ServicioRequestDTO;
import com.beautycare.api.controller.dto.ServicioResponseDTO;
import com.beautycare.api.service.ServicioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicios") // Define la URL base para este controlador
public class ServicioController {

    @Autowired
    private ServicioService servicioService; // Inyecta la interfaz

    /**
     * Endpoint para crear un nuevo servicio.
     * Solo accesible por ADMIN.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Seguridad a nivel de método
    public ResponseEntity<ServicioResponseDTO> createServicio(@Valid @RequestBody ServicioRequestDTO requestDTO) {
        ServicioResponseDTO nuevoServicio = servicioService.createServicio(requestDTO);
        // Devuelve un 201 Created
        return new ResponseEntity<>(nuevoServicio, HttpStatus.CREATED);
    }

    /**
     * Endpoint para obtener todos los servicios.
     * Accesible por cualquier usuario autenticado.
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()") // Seguridad a nivel de método
    public ResponseEntity<List<ServicioResponseDTO>> getAllServicios() {
        List<ServicioResponseDTO> servicios = servicioService.getAllServicios();
        return ResponseEntity.ok(servicios); // Devuelve 200 OK
    }

    /**
     * Endpoint para obtener un servicio por su ID.
     * Accesible por cualquier usuario autenticado.
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ServicioResponseDTO> getServicioById(@PathVariable Long id) {
        ServicioResponseDTO servicio = servicioService.getServicioById(id);
        return ResponseEntity.ok(servicio);
    }

    /**
     * Endpoint para actualizar un servicio.
     * Solo accesible por ADMIN.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServicioResponseDTO> updateServicio(@PathVariable Long id, @Valid @RequestBody ServicioRequestDTO requestDTO) {
        ServicioResponseDTO servicioActualizado = servicioService.updateServicio(id, requestDTO);
        return ResponseEntity.ok(servicioActualizado);
    }

    /**
     * Endpoint para eliminar un servicio.
     * Solo accesible por ADMIN.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteServicio(@PathVariable Long id) {
        servicioService.deleteServicio(id);
        return ResponseEntity.noContent().build(); // Devuelve 204 No Content
    }
}