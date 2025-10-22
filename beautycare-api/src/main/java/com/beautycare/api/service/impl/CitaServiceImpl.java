package com.beautycare.api.service.impl;

import com.beautycare.api.controller.dto.*; // Asegúrate que estén todos los DTOs
import com.beautycare.api.exception.ResourceNotFoundException;
import com.beautycare.api.exception.ValidationException;
import com.beautycare.api.model.Cita;
import com.beautycare.api.model.Cliente;
import com.beautycare.api.model.Profesional;
import com.beautycare.api.model.Servicio;
import com.beautycare.api.repository.CitaRepository;
import com.beautycare.api.repository.ClienteRepository;
import com.beautycare.api.repository.ProfesionalRepository;
import com.beautycare.api.repository.ServicioRepository;
import com.beautycare.api.service.CitaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest; // Importar PageRequest
import org.springframework.data.domain.Pageable; // Importar Pageable
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CitaServiceImpl implements CitaService {

    private static final Logger log = LoggerFactory.getLogger(CitaServiceImpl.class);

    @Autowired private CitaRepository citaRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private ProfesionalRepository profesionalRepository;
    @Autowired private ServicioRepository servicioRepository;

    @Autowired
    @Qualifier("inventoryWebClient")
    private WebClient webClient;

    private static final String ESTADO_PENDIENTE = "PENDIENTE";
    private static final String ESTADO_REALIZADA = "REALIZADA";

    // --- Métodos CRUD y updateEstado (sin cambios) ---
    @Override
    @Transactional
    public CitaResponseDTO createCita(CitaRequestDTO requestDTO) {
        Cliente cliente = clienteRepository.findById(requestDTO.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + requestDTO.getClienteId()));
        Profesional profesional = profesionalRepository.findById(requestDTO.getProfesionalId())
                .orElseThrow(() -> new ResourceNotFoundException("Profesional no encontrado con ID: " + requestDTO.getProfesionalId()));
        Servicio servicio = servicioRepository.findById(requestDTO.getServicioId())
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con ID: " + requestDTO.getServicioId()));

        LocalDateTime inicio = requestDTO.getFechaHoraInicio();
        LocalDateTime fin = inicio.plusMinutes(servicio.getDuracionMin());

        List<Cita> overlapping = citaRepository.findOverlappingCitas(inicio, fin, cliente.getId(), profesional.getId());
        if (!overlapping.isEmpty()) {
            throw new ValidationException("Conflicto de horario: Ya existe una cita para el cliente o profesional en ese intervalo.");
        }

        Cita nuevaCita = new Cita();
        nuevaCita.setCliente(cliente);
        nuevaCita.setProfesional(profesional);
        nuevaCita.setServicio(servicio);
        nuevaCita.setFechaHoraInicio(inicio);
        nuevaCita.setFechaHoraFin(fin);
        nuevaCita.setEstado(ESTADO_PENDIENTE);

        Cita citaGuardada = citaRepository.save(nuevaCita);
        return new CitaResponseDTO(citaGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public CitaResponseDTO getCitaById(Long id) {
        Cita cita = findCitaByIdOrThrow(id);
        return new CitaResponseDTO(cita);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaResponseDTO> getAllCitas() {
        List<Cita> citas = citaRepository.findAll();
        return citas.stream()
                .map(CitaResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CitaResponseDTO updateEstadoCita(Long id, CitaUpdateEstadoDTO updateEstadoDTO) {
        Cita citaExistente = findCitaByIdOrThrow(id);
        String nuevoEstado = updateEstadoDTO.getNuevoEstado();
        citaExistente.setEstado(nuevoEstado);
        Cita citaActualizada = citaRepository.save(citaExistente);

        if (ESTADO_REALIZADA.equalsIgnoreCase(nuevoEstado)) {
            Long servicioId = citaExistente.getServicio().getId();
            log.info("Cita {} marcada como REALIZADA. Llamando a inventory-service para descontar stock del servicio ID: {}", id, servicioId);
            webClient.post()
                    .uri("/api/inventory/consumo/registrar-por-servicio/{servicioId}", servicioId)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .doOnError(error -> log.error("Error al llamar a inventory-service para descontar stock: {}", error.getMessage()))
                    .doOnSuccess(response -> log.info("Llamada a inventory-service exitosa para servicio ID: {}", servicioId))
                    .subscribe();
        }
        return new CitaResponseDTO(citaActualizada);
    }

    @Override
    @Transactional
    public void deleteCita(Long id) {
        Cita cita = findCitaByIdOrThrow(id);
        citaRepository.delete(cita);
    }

    // --- NUEVO MÉTODO IMPLEMENTADO ---
    @Override
    @Transactional(readOnly = true) // Es una consulta de solo lectura
    public List<TopServicioDTO> getTopServiciosPorFecha(LocalDateTime desde, LocalDateTime hasta, int limit) {
        // 1. Crear el objeto Pageable para limitar los resultados
        // PageRequest.of(pagina, tamaño) -> usamos página 0 para los primeros 'limit' resultados
        Pageable pageable = PageRequest.of(0, limit);

        // 2. Llamar al método del repositorio pasando Pageable
        return citaRepository.findTopServiciosByEstadoAndFecha(ESTADO_REALIZADA, desde, hasta, pageable);
    }

    // --- Método de ayuda privado (sin cambios) ---
    private Cita findCitaByIdOrThrow(Long id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con ID: " + id));
    }
}