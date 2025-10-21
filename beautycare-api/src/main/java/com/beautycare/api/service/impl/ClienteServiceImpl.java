package com.beautycare.api.service.impl;

import com.beautycare.api.controller.dto.ClienteRequestDTO;
import com.beautycare.api.controller.dto.ClienteResponseDTO;
import com.beautycare.api.exception.ResourceNotFoundException;
import com.beautycare.api.model.Cliente;
import com.beautycare.api.repository.ClienteRepository;
import com.beautycare.api.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    @Transactional
    public ClienteResponseDTO createCliente(ClienteRequestDTO clienteRequestDTO) {
        // 1. Mapear DTO (Request) a Entidad
        Cliente cliente = new Cliente();
        cliente.setNombre(clienteRequestDTO.getNombre());
        cliente.setCelular(clienteRequestDTO.getCelular());
        cliente.setEmail(clienteRequestDTO.getEmail());

        // 2. Guardar en la BD usando el repositorio
        Cliente clienteGuardado = clienteRepository.save(cliente);

        // 3. Mapear Entidad a DTO (Response) y devolver
        return new ClienteResponseDTO(clienteGuardado);
    }

    @Override
    @Transactional(readOnly = true) // readOnly = true optimiza las consultas de solo lectura
    public ClienteResponseDTO getClienteById(Long id) {
        // 1. Buscar el cliente o lanzar excepción
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));

        // 2. Mapear a DTO (Response) y devolver
        return new ClienteResponseDTO(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> getAllClientes() {
        // 1. Buscar todos los clientes
        List<Cliente> clientes = clienteRepository.findAll();

        // 2. Mapear la lista de Entidades a una lista de DTOs (Response)
        return clientes.stream()
                .map(ClienteResponseDTO::new) // Usa el constructor que creamos en el DTO
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ClienteResponseDTO updateCliente(Long id, ClienteRequestDTO clienteRequestDTO) {
        // 1. Buscar el cliente a actualizar o lanzar excepción
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));

        // 2. Actualizar los campos de la entidad existente
        clienteExistente.setNombre(clienteRequestDTO.getNombre());
        clienteExistente.setCelular(clienteRequestDTO.getCelular());
        clienteExistente.setEmail(clienteRequestDTO.getEmail());

        // 3. Guardar los cambios (JPA detecta que es un UPDATE)
        Cliente clienteActualizado = clienteRepository.save(clienteExistente);

        // 4. Mapear a DTO (Response) y devolver
        return new ClienteResponseDTO(clienteActualizado);
    }

    @Override
    @Transactional
    public void deleteCliente(Long id) {
        // 1. Verificar si el cliente existe antes de borrar
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));

        // 2. Eliminar el cliente
        clienteRepository.delete(cliente);
    }
}