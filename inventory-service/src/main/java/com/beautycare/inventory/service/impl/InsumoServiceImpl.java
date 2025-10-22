package com.beautycare.inventory.service.impl;

import com.beautycare.inventory.controller.dto.InsumoRequestDTO;
import com.beautycare.inventory.controller.dto.InsumoResponseDTO;
import com.beautycare.inventory.exception.ResourceNotFoundException;
import com.beautycare.inventory.exception.ValidationException;
import com.beautycare.inventory.model.ConsumoInsumo;
import com.beautycare.inventory.model.Insumo;
import com.beautycare.inventory.repository.ConsumoInsumoRepository;
import com.beautycare.inventory.repository.InsumoRepository;
import com.beautycare.inventory.service.InsumoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InsumoServiceImpl implements InsumoService {

    private static final Logger log = LoggerFactory.getLogger(InsumoServiceImpl.class);

    @Autowired private InsumoRepository insumoRepository;
    @Autowired private ConsumoInsumoRepository consumoInsumoRepository;

    // --- Métodos CRUD (sin cambios) ---
    @Override
    @Transactional
    public InsumoResponseDTO createInsumo(InsumoRequestDTO requestDTO) {
        Insumo insumo = new Insumo();
        insumo.setNombre(requestDTO.getNombre());
        insumo.setStock(requestDTO.getStock());
        insumo.setStockMinimo(requestDTO.getStockMinimo());
        insumo.setUnidad(requestDTO.getUnidad());
        Insumo insumoGuardado = insumoRepository.save(insumo);
        return new InsumoResponseDTO(insumoGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public InsumoResponseDTO getInsumoById(Long id) {
        Insumo insumo = findInsumoByIdOrThrow(id);
        return new InsumoResponseDTO(insumo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InsumoResponseDTO> getAllInsumos() {
        List<Insumo> insumos = insumoRepository.findAll();
        return insumos.stream()
                .map(InsumoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InsumoResponseDTO updateInsumo(Long id, InsumoRequestDTO requestDTO) {
        Insumo insumoExistente = findInsumoByIdOrThrow(id);
        insumoExistente.setNombre(requestDTO.getNombre());
        insumoExistente.setStock(requestDTO.getStock());
        insumoExistente.setStockMinimo(requestDTO.getStockMinimo());
        insumoExistente.setUnidad(requestDTO.getUnidad());
        Insumo insumoActualizado = insumoRepository.save(insumoExistente);
        return new InsumoResponseDTO(insumoActualizado);
    }

    @Override
    @Transactional
    public void deleteInsumo(Long id) {
        Insumo insumo = findInsumoByIdOrThrow(id);
        insumoRepository.delete(insumo);
    }

    // --- Método de consumo (sin cambios) ---
    @Override
    @Transactional
    public void registrarConsumoPorServicio(Long servicioId) {
        log.info("Recibida solicitud para registrar consumo del servicio ID: {}", servicioId);
        List<ConsumoInsumo> consumos = consumoInsumoRepository.findByServicioId(servicioId);

        if (consumos.isEmpty()) {
            log.warn("No se encontraron consumos definidos para el servicio ID: {}. No se descontará stock.", servicioId);
            return;
        }

        for (ConsumoInsumo consumo : consumos) {
            Insumo insumo = findInsumoByIdOrThrow(consumo.getInsumo().getId());
            BigDecimal cantidadADescontar = consumo.getCantidadPorServicio();
            int stockActual = insumo.getStock();
            log.debug("Procesando consumo: Insumo ID: {}, Cantidad a descontar: {}, Stock actual: {}",
                    insumo.getId(), cantidadADescontar, stockActual);
            int cantidadInt = cantidadADescontar.intValue();

            if (stockActual < cantidadInt) {
                log.error("Stock insuficiente para el insumo ID: {}. Stock actual: {}, Cantidad requerida: {}",
                        insumo.getId(), stockActual, cantidadInt);
                throw new ValidationException("Stock insuficiente para el insumo: " + insumo.getNombre() +
                        ". Stock actual: " + stockActual + ", requerido: " + cantidadInt);
            }
            insumo.setStock(stockActual - cantidadInt);
            insumoRepository.save(insumo);
            log.info("Stock actualizado para insumo ID: {}. Nuevo stock: {}", insumo.getId(), insumo.getStock());
        }
    }

    @Override
    @Transactional(readOnly = true) // Es una consulta de solo lectura
    public List<InsumoResponseDTO> getInsumosBajoStock() {
        // 1. Llamar al método del repositorio
        List<Insumo> insumosBajoStock = insumoRepository.findInsumosBajoStock();

        // 2. Mapear la lista de Entidades a DTOs (Response)
        return insumosBajoStock.stream()
                .map(InsumoResponseDTO::new) // Usa el constructor del DTO
                .collect(Collectors.toList());
    }

    // --- Método de ayuda privado (sin cambios) ---
    private Insumo findInsumoByIdOrThrow(Long id) {
        return insumoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Insumo no encontrado con ID: " + id));
    }
}