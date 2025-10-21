package com.beautycare.inventory.service.impl;

import com.beautycare.inventory.controller.dto.InsumoRequestDTO;
import com.beautycare.inventory.controller.dto.InsumoResponseDTO;
import com.beautycare.inventory.exception.ResourceNotFoundException;
import com.beautycare.inventory.exception.ValidationException; // Importar la excepción
import com.beautycare.inventory.model.ConsumoInsumo; // Importar ConsumoInsumo
import com.beautycare.inventory.model.Insumo;
import com.beautycare.inventory.repository.ConsumoInsumoRepository; // Importar repo
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
    @Autowired private ConsumoInsumoRepository consumoInsumoRepository; // <-- Inyectar nuevo repo

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

    // --- NUEVO MÉTODO ---
    @Override
    @Transactional // ¡Importante! Debe ser transaccional
    public void registrarConsumoPorServicio(Long servicioId) {
        log.info("Recibida solicitud para registrar consumo del servicio ID: {}", servicioId);

        // 1. Buscar todos los consumos definidos para este servicio
        List<ConsumoInsumo> consumos = consumoInsumoRepository.findByServicioId(servicioId); // <-- Necesitamos crear este método en el repo

        if (consumos.isEmpty()) {
            log.warn("No se encontraron consumos definidos para el servicio ID: {}. No se descontará stock.", servicioId);
            return; // No hay nada que descontar
        }

        // 2. Iterar y descontar stock para cada insumo
        for (ConsumoInsumo consumo : consumos) {
            Insumo insumo = findInsumoByIdOrThrow(consumo.getInsumo().getId()); // Reutilizamos el método de ayuda
            BigDecimal cantidadADescontar = consumo.getCantidadPorServicio();
            int stockActual = insumo.getStock();

            log.debug("Procesando consumo: Insumo ID: {}, Cantidad a descontar: {}, Stock actual: {}",
                    insumo.getId(), cantidadADescontar, stockActual);

            // Convertimos la cantidad a descontar a int (asumiendo que el stock es entero)
            // En un caso real, si 'cantidadPorServicio' puede ser decimal, 'stock' también debería serlo.
            int cantidadInt = cantidadADescontar.intValue(); // Simplificación: asume cantidades enteras

            // 3. Validar stock suficiente
            if (stockActual < cantidadInt) {
                log.error("Stock insuficiente para el insumo ID: {}. Stock actual: {}, Cantidad requerida: {}",
                        insumo.getId(), stockActual, cantidadInt);
                throw new ValidationException("Stock insuficiente para el insumo: " + insumo.getNombre() +
                        ". Stock actual: " + stockActual + ", requerido: " + cantidadInt);
            }

            // 4. Descontar stock
            insumo.setStock(stockActual - cantidadInt);

            // 5. Guardar el insumo actualizado
            insumoRepository.save(insumo);
            log.info("Stock actualizado para insumo ID: {}. Nuevo stock: {}", insumo.getId(), insumo.getStock());
        }
    }

    // --- Método de ayuda privado (modificado para lanzar excepción correcta) ---
    private Insumo findInsumoByIdOrThrow(Long id) {
        return insumoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Insumo no encontrado con ID: " + id));
    }
}