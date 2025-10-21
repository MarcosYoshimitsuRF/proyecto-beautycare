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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CitaServiceImpl implements CitaService {

    @Autowired private CitaRepository citaRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private ProfesionalRepository profesionalRepository;
    @Autowired private ServicioRepository servicioRepository;

    // Constante para el estado inicial
    private static final String ESTADO_PENDIENTE = "PENDIENTE";

    @Override
    @Transactional
    public CitaResponseDTO createCita(CitaRequestDTO requestDTO) {
        // 1. Buscar entidades relacionadas (Cliente, Profesional, Servicio)
        Cliente cliente = clienteRepository.findById(requestDTO.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + requestDTO.getClienteId()));
        Profesional profesional = profesionalRepository.findById(requestDTO.getProfesionalId())
                .orElseThrow(() -> new ResourceNotFoundException("Profesional no encontrado con ID: " + requestDTO.getProfesionalId()));
        Servicio servicio = servicioRepository.findById(requestDTO.getServicioId())
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con ID: " + requestDTO.getServicioId()));

        // 2. Calcular fechaHoraFin
        LocalDateTime inicio = requestDTO.getFechaHoraInicio();
        LocalDateTime fin = inicio.plusMinutes(servicio.getDuracionMin());

        // 3. *** VALIDACIÓN CLAVE: Verificar solapamientos ***
        List<Cita> overlapping = citaRepository.findOverlappingCitas(inicio, fin, cliente.getId(), profesional.getId());
        if (!overlapping.isEmpty()) {
            throw new ValidationException("Conflicto de horario: Ya existe una cita para el cliente o profesional en ese intervalo.");
        }

        // 4. Crear la nueva entidad Cita
        Cita nuevaCita = new Cita();
        nuevaCita.setCliente(cliente);
        nuevaCita.setProfesional(profesional);
        nuevaCita.setServicio(servicio);
        nuevaCita.setFechaHoraInicio(inicio);
        nuevaCita.setFechaHoraFin(fin);
        nuevaCita.setEstado(ESTADO_PENDIENTE); // Estado inicial

        // 5. Guardar en la BD
        Cita citaGuardada = citaRepository.save(nuevaCita);

        // 6. Mapear a DTO (Response) y devolver
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

        // 2. Actualizar el estado
        citaExistente.setEstado(updateEstadoDTO.getNuevoEstado());

        // 3. Guardar cambios
        Cita citaActualizada = citaRepository.save(citaExistente);

        // (NOTA: Aquí, en la Fase 5, añadiremos la lógica para llamar al
        // microservicio de inventario si el nuevoEstado es "REALIZADA")

        // 4. Mapear a DTO (Response) y devolver
        return new CitaResponseDTO(citaActualizada);
    }

    @Override
    @Transactional
    public void deleteCita(Long id) {
        // 1. Verificar si existe
        Cita cita = findCitaByIdOrThrow(id);
        // 2. Eliminar
        citaRepository.delete(cita);
    }

    // --- Método de ayuda privado ---
    private Cita findCitaByIdOrThrow(Long id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con ID: " + id));
    }
}