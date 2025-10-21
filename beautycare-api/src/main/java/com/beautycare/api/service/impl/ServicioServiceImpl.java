package com.beautycare.api.service.impl;

import com.beautycare.api.controller.dto.ServicioRequestDTO;
import com.beautycare.api.controller.dto.ServicioResponseDTO;
import com.beautycare.api.exception.ResourceNotFoundException;
import com.beautycare.api.model.Servicio;
import com.beautycare.api.repository.ServicioRepository;
import com.beautycare.api.service.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicioServiceImpl implements ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;

    @Override
    @Transactional
    public ServicioResponseDTO createServicio(ServicioRequestDTO requestDTO) {
        // 1. Mapear DTO (Request) a Entidad
        Servicio servicio = new Servicio();
        servicio.setNombre(requestDTO.getNombre());
        servicio.setPrecio(requestDTO.getPrecio());
        servicio.setDuracionMin(requestDTO.getDuracionMin());

        // 2. Guardar en la BD
        Servicio servicioGuardado = servicioRepository.save(servicio);

        // 3. Mapear Entidad a DTO (Response) y devolver
        return new ServicioResponseDTO(servicioGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public ServicioResponseDTO getServicioById(Long id) {
        // 1. Buscar o lanzar excepción
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con ID: " + id));

        // 2. Mapear a DTO (Response) y devolver
        return new ServicioResponseDTO(servicio);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServicioResponseDTO> getAllServicios() {
        // 1. Buscar todos
        List<Servicio> servicios = servicioRepository.findAll();

        // 2. Mapear la lista de Entidades a DTOs (Response)
        return servicios.stream()
                .map(ServicioResponseDTO::new) // Usa el constructor del DTO
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ServicioResponseDTO updateServicio(Long id, ServicioRequestDTO requestDTO) {
        // 1. Buscar el servicio a actualizar o lanzar excepción
        Servicio servicioExistente = servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con ID: " + id));

        // 2. Actualizar los campos
        servicioExistente.setNombre(requestDTO.getNombre());
        servicioExistente.setPrecio(requestDTO.getPrecio());
        servicioExistente.setDuracionMin(requestDTO.getDuracionMin());

        // 3. Guardar los cambios (JPA detecta UPDATE)
        Servicio servicioActualizado = servicioRepository.save(servicioExistente);

        // 4. Mapear a DTO (Response) y devolver
        return new ServicioResponseDTO(servicioActualizado);
    }

    @Override
    @Transactional
    public void deleteServicio(Long id) {
        // 1. Verificar si existe
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con ID: " + id));

        // 2. Eliminar
        servicioRepository.delete(servicio);
    }
}