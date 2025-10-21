package com.beautycare.api.service.impl;

import com.beautycare.api.controller.dto.ProfesionalRequestDTO;
import com.beautycare.api.controller.dto.ProfesionalResponseDTO;
import com.beautycare.api.exception.ResourceNotFoundException;
import com.beautycare.api.model.Profesional;
import com.beautycare.api.repository.ProfesionalRepository;
import com.beautycare.api.service.ProfesionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfesionalServiceImpl implements ProfesionalService {

    @Autowired
    private ProfesionalRepository profesionalRepository;

    @Override
    @Transactional
    public ProfesionalResponseDTO createProfesional(ProfesionalRequestDTO requestDTO) {
        // 1. Mapear DTO (Request) a Entidad
        Profesional profesional = new Profesional();
        profesional.setNombre(requestDTO.getNombre());
        profesional.setEspecialidad(requestDTO.getEspecialidad());

        // 2. Guardar en la BD
        Profesional profesionalGuardado = profesionalRepository.save(profesional);

        // 3. Mapear Entidad a DTO (Response) y devolver
        return new ProfesionalResponseDTO(profesionalGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public ProfesionalResponseDTO getProfesionalById(Long id) {
        // 1. Buscar o lanzar excepción
        Profesional profesional = profesionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profesional no encontrado con ID: " + id));

        // 2. Mapear a DTO (Response) y devolver
        return new ProfesionalResponseDTO(profesional);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProfesionalResponseDTO> getAllProfesionales() {
        // 1. Buscar todos
        List<Profesional> profesionales = profesionalRepository.findAll();

        // 2. Mapear la lista de Entidades a DTOs (Response)
        return profesionales.stream()
                .map(ProfesionalResponseDTO::new) // Usa el constructor del DTO
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProfesionalResponseDTO updateProfesional(Long id, ProfesionalRequestDTO requestDTO) {
        // 1. Buscar el profesional a actualizar o lanzar excepción
        Profesional profesionalExistente = profesionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profesional no encontrado con ID: " + id));

        // 2. Actualizar los campos
        profesionalExistente.setNombre(requestDTO.getNombre());
        profesionalExistente.setEspecialidad(requestDTO.getEspecialidad());

        // 3. Guardar los cambios (JPA detecta UPDATE)
        Profesional profesionalActualizado = profesionalRepository.save(profesionalExistente);

        // 4. Mapear a DTO (Response) y devolver
        return new ProfesionalResponseDTO(profesionalActualizado);
    }

    @Override
    @Transactional
    public void deleteProfesional(Long id) {
        // 1. Verificar si existe
        Profesional profesional = profesionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profesional no encontrado con ID: " + id));

        // 2. Eliminar
        profesionalRepository.delete(profesional);
    }
}