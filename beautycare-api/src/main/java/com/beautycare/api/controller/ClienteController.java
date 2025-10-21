package com.beautycare.api.controller;

import com.beautycare.api.controller.dto.ClienteRequestDTO;
import com.beautycare.api.controller.dto.ClienteResponseDTO;
import com.beautycare.api.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes") // Define la URL base para este controlador
public class ClienteController {

    @Autowired
    private ClienteService clienteService; // Inyecta la interfaz, no la implementación

    /**
     * Endpoint para crear un nuevo cliente.
     * Solo accesible por ADMIN.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Seguridad a nivel de método
    public ResponseEntity<ClienteResponseDTO> createCliente(@Valid @RequestBody ClienteRequestDTO clienteRequestDTO) {
        ClienteResponseDTO nuevoCliente = clienteService.createCliente(clienteRequestDTO);
        // Devuelve un 201 Created
        return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
    }

    /**
     * Endpoint para obtener todos los clientes.
     * Accesible por cualquier usuario autenticado.
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()") // Seguridad a nivel de método
    public ResponseEntity<List<ClienteResponseDTO>> getAllClientes() {
        List<ClienteResponseDTO> clientes = clienteService.getAllClientes();
        return ResponseEntity.ok(clientes); // Devuelve 200 OK
    }

    /**
     * Endpoint para obtener un cliente por su ID.
     * Accesible por cualquier usuario autenticado.
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ClienteResponseDTO> getClienteById(@PathVariable Long id) {
        ClienteResponseDTO cliente = clienteService.getClienteById(id);
        return ResponseEntity.ok(cliente);
    }

    /**
     * Endpoint para actualizar un cliente.
     * Solo accesible por ADMIN.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteResponseDTO> updateCliente(@PathVariable Long id, @Valid @RequestBody ClienteRequestDTO clienteRequestDTO) {
        ClienteResponseDTO clienteActualizado = clienteService.updateCliente(id, clienteRequestDTO);
        return ResponseEntity.ok(clienteActualizado);
    }

    /**
     * Endpoint para eliminar un cliente.
     * Solo accesible por ADMIN.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        clienteService.deleteCliente(id);
        return ResponseEntity.noContent().build(); // Devuelve 204 No Content
    }
}