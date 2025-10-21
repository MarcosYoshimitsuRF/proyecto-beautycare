package com.beautycare.api.service.impl;

import com.beautycare.api.controller.dto.PagoRequestDTO;
import com.beautycare.api.controller.dto.PagoResponseDTO;
import com.beautycare.api.exception.ResourceNotFoundException;
import com.beautycare.api.model.Cita;
import com.beautycare.api.model.Pago;
import com.beautycare.api.repository.CitaRepository;
import com.beautycare.api.repository.PagoRepository;
import com.beautycare.api.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PagoServiceImpl implements PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private CitaRepository citaRepository; // Necesario para buscar la cita asociada

    @Override
    @Transactional
    public PagoResponseDTO createPago(PagoRequestDTO requestDTO) {
        // 1. Buscar la cita asociada o lanzar excepción
        Cita cita = citaRepository.findById(requestDTO.getCitaId())
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con ID: " + requestDTO.getCitaId()));

        // (NOTA: Aquí se podría añadir validación para evitar doble pago si es necesario)

        // 2. Mapear DTO (Request) a Entidad
        Pago pago = new Pago();
        pago.setCita(cita);
        pago.setMonto(requestDTO.getMonto());
        pago.setMetodo(requestDTO.getMetodo());
        pago.setFechaHora(LocalDateTime.now()); // Establecer la fecha/hora actual

        // 3. Guardar en la BD
        Pago pagoGuardado = pagoRepository.save(pago);

        // 4. Mapear Entidad a DTO (Response) y devolver
        return new PagoResponseDTO(pagoGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public PagoResponseDTO getPagoById(Long id) {
        // 1. Buscar o lanzar excepción
        Pago pago = findPagoByIdOrThrow(id);

        // 2. Mapear a DTO (Response) y devolver
        return new PagoResponseDTO(pago);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoResponseDTO> getAllPagos() {
        // 1. Buscar todos
        List<Pago> pagos = pagoRepository.findAll();

        // 2. Mapear la lista de Entidades a DTOs (Response)
        return pagos.stream()
                .map(PagoResponseDTO::new) // Usa el constructor del DTO
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PagoResponseDTO updatePago(Long id, PagoRequestDTO requestDTO) {
        // 1. Buscar el pago a actualizar o lanzar excepción
        Pago pagoExistente = findPagoByIdOrThrow(id);

        // 2. Buscar la nueva cita asociada (si cambió)
        Cita cita = citaRepository.findById(requestDTO.getCitaId())
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con ID: " + requestDTO.getCitaId()));

        // 3. Actualizar los campos
        pagoExistente.setCita(cita);
        pagoExistente.setMonto(requestDTO.getMonto());
        pagoExistente.setMetodo(requestDTO.getMetodo());
        // No actualizamos fechaHora, asumimos que es inmutable

        // 4. Guardar los cambios (JPA detecta UPDATE)
        Pago pagoActualizado = pagoRepository.save(pagoExistente);

        // (NOTA: Considerar si los pagos deben ser actualizables)

        // 5. Mapear a DTO (Response) y devolver
        return new PagoResponseDTO(pagoActualizado);
    }

    @Override
    @Transactional
    public void deletePago(Long id) {
        // 1. Verificar si existe
        Pago pago = findPagoByIdOrThrow(id);

        // (NOTA: Considerar si eliminar un pago debe tener otras implicaciones)

        // 2. Eliminar
        pagoRepository.delete(pago);
    }

    // --- Método de ayuda privado ---
    private Pago findPagoByIdOrThrow(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado con ID: " + id));
    }
}