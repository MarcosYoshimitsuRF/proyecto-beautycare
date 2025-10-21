package com.beautycare.inventory.service.impl;

import com.beautycare.inventory.controller.dto.InsumoRequestDTO;
import com.beautycare.inventory.controller.dto.InsumoResponseDTO;
import com.beautycare.inventory.exception.ResourceNotFoundException;
import com.beautycare.inventory.model.Insumo;
import com.beautycare.inventory.repository.InsumoRepository;
import com.beautycare.inventory.service.InsumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InsumoServiceImpl implements InsumoService {

    @Autowired
    private InsumoRepository insumoRepository;

    @Override
    @Transactional
    public InsumoResponseDTO createInsumo(InsumoRequestDTO requestDTO) {
        // 1. Mapear DTO (Request) a Entidad
        Insumo insumo = new Insumo();
        insumo.setNombre(requestDTO.getNombre());
        insumo.setStock(requestDTO.getStock());
        insumo.setStockMinimo(requestDTO.getStockMinimo());
        insumo.setUnidad(requestDTO.getUnidad());

        // 2. Guardar en la BD
        Insumo insumoGuardado = insumoRepository.save(insumo);

        // 3. Mapear Entidad a DTO (Response) y devolver
        return new InsumoResponseDTO(insumoGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public InsumoResponseDTO getInsumoById(Long id) {
        // 1. Buscar o lanzar excepción
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Insumo no encontrado con ID: " + id));

        // 2. Mapear a DTO (Response) y devolver
        return new InsumoResponseDTO(insumo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InsumoResponseDTO> getAllInsumos() {
        // 1. Buscar todos
        List<Insumo> insumos = insumoRepository.findAll();

        // 2. Mapear la lista de Entidades a DTOs (Response)
        return insumos.stream()
                .map(InsumoResponseDTO::new) // Usa el constructor del DTO
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InsumoResponseDTO updateInsumo(Long id, InsumoRequestDTO requestDTO) {
        // 1. Buscar el insumo a actualizar o lanzar excepción
        Insumo insumoExistente = insumoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Insumo no encontrado con ID: " + id));

        // 2. Actualizar los campos
        insumoExistente.setNombre(requestDTO.getNombre());
        insumoExistente.setStock(requestDTO.getStock());
        insumoExistente.setStockMinimo(requestDTO.getStockMinimo());
        insumoExistente.setUnidad(requestDTO.getUnidad());

        // 3. Guardar los cambios (JPA detecta UPDATE)
        Insumo insumoActualizado = insumoRepository.save(insumoExistente);

        // 4. Mapear a DTO (Response) y devolver
        return new InsumoResponseDTO(insumoActualizado);
    }

    @Override
    @Transactional
    public void deleteInsumo(Long id) {
        // 1. Verificar si existe
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Insumo no encontrado con ID: " + id));

        // 2. Eliminar
        insumoRepository.delete(insumo);
    }
}