package com.beautycare.inventory.service;

import com.beautycare.inventory.controller.dto.CompraRequestDTO;
import com.beautycare.inventory.controller.dto.CompraResponseDTO;

import java.util.List;

/**
 * Interfaz (contrato) para la lógica de negocio relacionada con Compras.
 */
public interface CompraService {

    /**
     * Crea un nuevo registro de compra.
     * (NOTA: La lógica de reponer stock se añade aquí o en un paso separado)
     *
     * @param requestDTO Datos de la compra a crear.
     * @return La compra guardada con su ID.
     */
    CompraResponseDTO createCompra(CompraRequestDTO requestDTO);

    /**
     * Obtiene una compra por su ID.
     *
     * @param id El ID de la compra a buscar.
     * @return La compra encontrada.
     * @throws com.beautycare.inventory.exception.ResourceNotFoundException Si no se encuentra.
     */
    CompraResponseDTO getCompraById(Long id);

    /**
     * Devuelve una lista de todas las compras.
     * (Opcional: Podría tener filtros por fecha o proveedor).
     *
     * @return Lista de compras.
     */
    List<CompraResponseDTO> getAllCompras();

    /**
     * Actualiza una compra existente.
     * (Nota: Considerar si las compras deben ser inmutables después de creadas).
     *
     * @param id El ID de la compra a actualizar.
     * @param requestDTO Los nuevos datos para la compra.
     * @return La compra con los datos actualizados.
     * @throws com.beautycare.inventory.exception.ResourceNotFoundException Si no se encuentra.
     */
    CompraResponseDTO updateCompra(Long id, CompraRequestDTO requestDTO);

    /**
     * Elimina una compra por su ID.
     * (Nota: Considerar si las compras deben poder eliminarse).
     *
     * @param id El ID de la compra a eliminar.
     * @throws com.beautycare.inventory.exception.ResourceNotFoundException Si no se encuentra.
     */
    void deleteCompra(Long id);
}