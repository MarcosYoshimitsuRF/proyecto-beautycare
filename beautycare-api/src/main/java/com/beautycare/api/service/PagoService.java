package com.beautycare.api.service;

import com.beautycare.api.controller.dto.PagoRequestDTO;
import com.beautycare.api.controller.dto.PagoResponseDTO;

import java.util.List;

/**
 * Interfaz (contrato) para la lógica de negocio relacionada con Pagos.
 */
public interface PagoService {

    /**
     * Crea un nuevo registro de pago para una cita.
     * La fechaHora se establecerá automáticamente.
     * (NOTA: Considerar si debe validar doble pago para la misma cita).
     *
     * @param requestDTO Datos del pago a crear.
     * @return El pago guardado con su ID y fechaHora.
     * @throws com.beautycare.api.exception.ResourceNotFoundException Si la cita no se encuentra.
     */
    PagoResponseDTO createPago(PagoRequestDTO requestDTO);

    /**
     * Obtiene un pago por su ID.
     *
     * @param id El ID del pago a buscar.
     * @return El pago encontrado.
     * @throws com.beautycare.api.exception.ResourceNotFoundException Si no se encuentra.
     */
    PagoResponseDTO getPagoById(Long id);

    /**
     * Devuelve una lista de todos los pagos.
     * (Opcional: Podría tener filtros por fecha, cliente, método).
     *
     * @return Lista de pagos.
     */
    List<PagoResponseDTO> getAllPagos();

    /**
     * Actualiza un pago existente.
     * (Nota: Considerar si los pagos deben ser inmutables después de creados).
     *
     * @param id El ID del pago a actualizar.
     * @param requestDTO Los nuevos datos para el pago (ej. cambiar método).
     * @return El pago con los datos actualizados.
     * @throws com.beautycare.api.exception.ResourceNotFoundException Si no se encuentra.
     */
    PagoResponseDTO updatePago(Long id, PagoRequestDTO requestDTO);

    /**
     * Elimina un pago por su ID.
     * (Nota: Considerar si los pagos deben poder eliminarse).
     *
     * @param id El ID del pago a eliminar.
     * @throws com.beautycare.api.exception.ResourceNotFoundException Si no se encuentra.
     */
    void deletePago(Long id);
}