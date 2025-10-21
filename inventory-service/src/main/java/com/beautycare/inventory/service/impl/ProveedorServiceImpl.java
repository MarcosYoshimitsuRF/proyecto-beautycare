package com.beautycare.inventory.service.impl;

import com.beautycare.inventory.controller.dto.ProveedorRequestDTO;
import com.beautycare.inventory.controller.dto.ProveedorResponseDTO;
import com.beautycare.inventory.exception.ResourceNotFoundException;
import com.beautycare.inventory.model.Proveedor;
import com.beautycare.inventory.repository.ProveedorRepository;
import com.beautycare.inventory.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProveedorServiceImpl implements ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Override
    @Transactional
    public ProveedorResponseDTO createProveedor(ProveedorRequestDTO requestDTO) {
        // 1. Mapear DTO (Request) a Entidad
        Proveedor proveedor = new Proveedor();
        proveedor.setNombre(requestDTO.getNombre());
        proveedor.setRuc(requestDTO.getRuc());
        proveedor.setTelefono(requestDTO.getTelefono());

        // 2. Guardar en la BD
        Proveedor proveedorGuardado = proveedorRepository.save(proveedor);

        // 3. Mapear Entidad a DTO (Response) y devolver
        return new ProveedorResponseDTO(proveedorGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public ProveedorResponseDTO getProveedorById(Long id) {
        // 1. Buscar o lanzar excepción
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con ID: " + id));

        // 2. Mapear a DTO (Response) y devolver
        return new ProveedorResponseDTO(proveedor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorResponseDTO> getAllProveedores() {
        // 1. Buscar todos
        List<Proveedor> proveedores = proveedorRepository.findAll();

        // 2. Mapear la lista de Entidades a DTOs (Response)
        return proveedores.stream()
                .map(ProveedorResponseDTO::new) // Usa el constructor del DTO
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProveedorResponseDTO updateProveedor(Long id, ProveedorRequestDTO requestDTO) {
        // 1. Buscar el proveedor a actualizar o lanzar excepción
        Proveedor proveedorExistente = proveedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con ID: " + id));

        // 2. Actualizar los campos
        proveedorExistente.setNombre(requestDTO.getNombre());
        proveedorExistente.setRuc(requestDTO.getRuc());
        proveedorExistente.setTelefono(requestDTO.getTelefono());

        // 3. Guardar los cambios (JPA detecta UPDATE)
        Proveedor proveedorActualizado = proveedorRepository.save(proveedorExistente);

        // 4. Mapear a DTO (Response) y devolver
        return new ProveedorResponseDTO(proveedorActualizado);
    }

    @Override
    @Transactional
    public void deleteProveedor(Long id) {
        // 1. Verificar si existe
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con ID: " + id));

        // 2. Eliminar
        proveedorRepository.delete(proveedor);
    }
}