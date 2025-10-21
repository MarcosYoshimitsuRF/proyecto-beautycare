package com.beautycare.api.controller;

import com.beautycare.api.controller.dto.PagoRequestDTO;
import com.beautycare.api.controller.dto.PagoResponseDTO;
import com.beautycare.api.service.PagoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagos") // Define la URL base para este controlador
@PreAuthorize("hasRole('ADMIN')") // Seguridad a nivel de clase: Todo aquí es solo para ADMIN
public class PagoController {

    @Autowired
    private PagoService pagoService; // Inyecta la interfaz

    /**
     * Endpoint para crear un nuevo registro de pago.
     * Solo accesible por ADMIN.
     */
    @PostMapping
    public ResponseEntity<PagoResponseDTO> createPago(@Valid @RequestBody PagoRequestDTO requestDTO) {
        PagoResponseDTO nuevoPago = pagoService.createPago(requestDTO);
        // Devuelve un 201 Created
        return new ResponseEntity<>(nuevoPago, HttpStatus.CREATED);
    }

    /**
     * Endpoint para obtener todos los registros de pago.
     * Solo accesible por ADMIN.
     */
    @GetMapping
    public ResponseEntity<List<PagoResponseDTO>> getAllPagos() {
        List<PagoResponseDTO> pagos = pagoService.getAllPagos();
        return ResponseEntity.ok(pagos); // Devuelve 200 OK
    }

    /**
     * Endpoint para obtener un registro de pago por su ID.
     * Solo accesible por ADMIN.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PagoResponseDTO> getPagoById(@PathVariable Long id) {
        PagoResponseDTO pago = pagoService.getPagoById(id);
        return ResponseEntity.ok(pago);
    }

    /**
     * Endpoint para actualizar un registro de pago.
     * Solo accesible por ADMIN.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PagoResponseDTO> updatePago(@PathVariable Long id, @Valid @RequestBody PagoRequestDTO requestDTO) {
        PagoResponseDTO pagoActualizado = pagoService.updatePago(id, requestDTO);
        return ResponseEntity.ok(pagoActualizado);
    }

    /**
     * Endpoint para eliminar un registro de pago.
     * Solo accesible por ADMIN.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePago(@PathVariable Long id) {
        pagoService.deletePago(id);
        return ResponseEntity.noContent().build(); // Devuelve 204 No Content
    }
}