package com.beautycare.api.controller;

import com.beautycare.api.controller.dto.CitaRequestDTO;
import com.beautycare.api.controller.dto.CitaResponseDTO;
import com.beautycare.api.controller.dto.CitaUpdateEstadoDTO;
import com.beautycare.api.service.CitaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/citas") // Define la URL base para este controlador
public class CitaController {

    @Autowired
    private CitaService citaService; // Inyecta la interfaz

    /**
     * Endpoint para crear una nueva cita.
     * Accesible por cualquier usuario autenticado (Cliente o Admin).
     * La validación de solapamiento está en el servicio.
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()") // Seguridad: Cualquier usuario logueado puede agendar
    public ResponseEntity<CitaResponseDTO> createCita(@Valid @RequestBody CitaRequestDTO requestDTO) {
        CitaResponseDTO nuevaCita = citaService.createCita(requestDTO);
        // Devuelve un 201 Created
        return new ResponseEntity<>(nuevaCita, HttpStatus.CREATED);
    }

    /**
     * Endpoint para obtener todas las citas.
     * Accesible por cualquier usuario autenticado.
     * (Se podría añadir lógica para que Cliente vea solo las suyas, y Admin todas).
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CitaResponseDTO>> getAllCitas() {
        // TODO: Implementar lógica de roles si es necesario (Cliente vs Admin)
        List<CitaResponseDTO> citas = citaService.getAllCitas();
        return ResponseEntity.ok(citas); // Devuelve 200 OK
    }

    /**
     * Endpoint para obtener una cita por su ID.
     * Accesible por cualquier usuario autenticado.
     * (Se podría añadir lógica para que Cliente vea solo las suyas).
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CitaResponseDTO> getCitaById(@PathVariable Long id) {
        // TODO: Implementar lógica de roles si es necesario
        CitaResponseDTO cita = citaService.getCitaById(id);
        return ResponseEntity.ok(cita);
    }

    /**
     * Endpoint para actualizar el ESTADO de una cita.
     * Usado principalmente por Admin o Staff (ej. marcar como 'REALIZADA').
     * Solo accesible por ADMIN (según documentación /admin/**)
     */
    @PutMapping("/{id}/estado") // Endpoint específico para cambiar estado
    @PreAuthorize("hasRole('ADMIN')") // Solo Admin puede cambiar estados críticos
    public ResponseEntity<CitaResponseDTO> updateEstadoCita(@PathVariable Long id, @Valid @RequestBody CitaUpdateEstadoDTO updateEstadoDTO) {
        CitaResponseDTO citaActualizada = citaService.updateEstadoCita(id, updateEstadoDTO);
        return ResponseEntity.ok(citaActualizada);
    }

    /**
     * Endpoint para eliminar una cita.
     * Solo accesible por ADMIN.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCita(@PathVariable Long id) {
        citaService.deleteCita(id);
        return ResponseEntity.noContent().build(); // Devuelve 204 No Content
    }
}