package com.beautycare.api.controller;

import com.beautycare.api.controller.dto.ProfesionalRequestDTO;
import com.beautycare.api.controller.dto.ProfesionalResponseDTO;
import com.beautycare.api.service.ProfesionalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profesionales") // Define la URL base para este controlador
public class ProfesionalController {

    @Autowired
    private ProfesionalService profesionalService; // Inyecta la interfaz

    /**
     * Endpoint para crear un nuevo profesional.
     * Solo accesible por ADMIN.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Seguridad a nivel de método
    public ResponseEntity<ProfesionalResponseDTO> createProfesional(@Valid @RequestBody ProfesionalRequestDTO requestDTO) {
        ProfesionalResponseDTO nuevoProfesional = profesionalService.createProfesional(requestDTO);
        // Devuelve un 201 Created
        return new ResponseEntity<>(nuevoProfesional, HttpStatus.CREATED);
    }

    /**
     * Endpoint para obtener todos los profesionales.
     * Accesible por cualquier usuario autenticado.
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()") // Seguridad a nivel de método
    public ResponseEntity<List<ProfesionalResponseDTO>> getAllProfesionales() {
        List<ProfesionalResponseDTO> profesionales = profesionalService.getAllProfesionales();
        return ResponseEntity.ok(profesionales); // Devuelve 200 OK
    }

    /**
     * Endpoint para obtener un profesional por su ID.
     * Accesible por cualquier usuario autenticado.
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ProfesionalResponseDTO> getProfesionalById(@PathVariable Long id) {
        ProfesionalResponseDTO profesional = profesionalService.getProfesionalById(id);
        return ResponseEntity.ok(profesional);
    }

    /**
     * Endpoint para actualizar un profesional.
     * Solo accesible por ADMIN.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProfesionalResponseDTO> updateProfesional(@PathVariable Long id, @Valid @RequestBody ProfesionalRequestDTO requestDTO) {
        ProfesionalResponseDTO profesionalActualizado = profesionalService.updateProfesional(id, requestDTO);
        return ResponseEntity.ok(profesionalActualizado);
    }

    /**
     * Endpoint para eliminar un profesional.
     * Solo accesible por ADMIN.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProfesional(@PathVariable Long id) {
        profesionalService.deleteProfesional(id);
        return ResponseEntity.noContent().build(); // Devuelve 204 No Content
    }
}