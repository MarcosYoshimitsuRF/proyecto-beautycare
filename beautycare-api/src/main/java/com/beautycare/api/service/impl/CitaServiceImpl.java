package com.beautycare.api.service.impl;

import com.beautycare.api.controller.dto.CitaRequestDTO;
import com.beautycare.api.controller.dto.CitaResponseDTO;
import com.beautycare.api.controller.dto.CitaUpdateEstadoDTO;
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
import org.springframework.beans.factory.annotation.Qualifier; // Necesario para WebClient Bean
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono; // Necesario para WebClient reactivo

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

    // 1. Inyectar el WebClient configurado
    @Autowired
    @Qualifier("inventoryWebClient") // Especifica cuál Bean de WebClient usar
    private WebClient webClient;

    private static final String ESTADO_PENDIENTE = "PENDIENTE";
    private static final String ESTADO_REALIZADA = "REALIZADA"; // Constante para el estado clave

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
        // 1. Buscar la cita
        Cita citaExistente = findCitaByIdOrThrow(id);
        String nuevoEstado = updateEstadoDTO.getNuevoEstado();

        // 2. Actualizar el estado
        citaExistente.setEstado(nuevoEstado);

        // 3. Guardar cambios en la BD
        Cita citaActualizada = citaRepository.save(citaExistente);

        // 4. *** LLAMADA AL MICROSERVICIO (SI ES NECESARIO) ***
        if (ESTADO_REALIZADA.equalsIgnoreCase(nuevoEstado)) {
            Long servicioId = citaExistente.getServicio().getId();
            log.info("Cita {} marcada como REALIZADA. Llamando a inventory-service para descontar stock del servicio ID: {}", id, servicioId);

            // Hacer la llamada POST al endpoint definido en el roadmap
            webClient.post()
                    .uri("/api/inventory/consumo/registrar-por-servicio/{servicioId}", servicioId) // Construye la URL completa
                    .retrieve() // Ejecuta la petición
                    .bodyToMono(Void.class) // Espera una respuesta vacía (o puedes mapear a otra clase si devuelve algo)
                    .doOnError(error -> log.error("Error al llamar a inventory-service para descontar stock: {}", error.getMessage()))
                    .doOnSuccess(response -> log.info("Llamada a inventory-service exitosa para servicio ID: {}", servicioId))
                    .subscribe(); // Necesario para ejecutar la llamada reactiva (no bloqueante)
        }

        // 5. Mapear a DTO (Response) y devolver
        return new CitaResponseDTO(citaActualizada);
    }

    @Override
    @Transactional
    public void deleteCita(Long id) {
        Cita cita = findCitaByIdOrThrow(id);
        citaRepository.delete(cita);
    }

    private Cita findCitaByIdOrThrow(Long id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con ID: " + id));
    }
}