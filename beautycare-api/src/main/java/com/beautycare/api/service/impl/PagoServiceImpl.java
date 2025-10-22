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

import java.math.BigDecimal; // Asegúrate que BigDecimal esté importado
import java.time.LocalDateTime; // Asegúrate que LocalDateTime esté importado
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PagoServiceImpl implements PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private CitaRepository citaRepository;

    // --- Métodos CRUD (sin cambios) ---
    @Override
    @Transactional
    public PagoResponseDTO createPago(PagoRequestDTO requestDTO) {
        Cita cita = citaRepository.findById(requestDTO.getCitaId())
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con ID: " + requestDTO.getCitaId()));
        Pago pago = new Pago();
        pago.setCita(cita);
        pago.setMonto(requestDTO.getMonto());
        pago.setMetodo(requestDTO.getMetodo());
        pago.setFechaHora(LocalDateTime.now());
        Pago pagoGuardado = pagoRepository.save(pago);
        return new PagoResponseDTO(pagoGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public PagoResponseDTO getPagoById(Long id) {
        Pago pago = findPagoByIdOrThrow(id);
        return new PagoResponseDTO(pago);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoResponseDTO> getAllPagos() {
        List<Pago> pagos = pagoRepository.findAll();
        return pagos.stream()
                .map(PagoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PagoResponseDTO updatePago(Long id, PagoRequestDTO requestDTO) {
        Pago pagoExistente = findPagoByIdOrThrow(id);
        Cita cita = citaRepository.findById(requestDTO.getCitaId())
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con ID: " + requestDTO.getCitaId()));
        pagoExistente.setCita(cita);
        pagoExistente.setMonto(requestDTO.getMonto());
        pagoExistente.setMetodo(requestDTO.getMetodo());
        Pago pagoActualizado = pagoRepository.save(pagoExistente);
        return new PagoResponseDTO(pagoActualizado);
    }

    @Override
    @Transactional
    public void deletePago(Long id) {
        Pago pago = findPagoByIdOrThrow(id);
        pagoRepository.delete(pago);
    }

    // --- NUEVO MeTODO IMPLEMENTADO ---
    @Override
    @Transactional(readOnly = true) // Es una consulta de solo lectura
    public BigDecimal getTotalIngresosPorFecha(LocalDateTime desde, LocalDateTime hasta) {
        // Llama directamente al método del repositorio que hace el cálculo
        return pagoRepository.sumMontoBetweenDates(desde, hasta);
    }

    // --- Metodo de ayuda privado (sin cambios) ---
    private Pago findPagoByIdOrThrow(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado con ID: " + id));
    }
}