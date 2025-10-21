package com.beautycare.inventory.service;

import com.beautycare.inventory.controller.dto.ProveedorRequestDTO;
import com.beautycare.inventory.controller.dto.ProveedorResponseDTO;

import java.util.List;

/**
 * Interfaz (contrato) para la lógica de negocio relacionada con Proveedores.
 */
public interface ProveedorService {

    /**
     * Crea un nuevo proveedor.
     *
     * @param requestDTO Datos del proveedor a crear.
     * @return El proveedor guardado con su ID.
     */
    ProveedorResponseDTO createProveedor(ProveedorRequestDTO requestDTO);

    /**
     * Obtiene un proveedor por su ID.
     *
     * @param id El ID del proveedor a buscar.
     * @return El proveedor encontrado.
     * @throws com.beautycare.inventory.exception.ResourceNotFoundException Si no se encuentra.
     */
    ProveedorResponseDTO getProveedorById(Long id);

    /**
     * Devuelve una lista de todos los proveedores.
     *
     * @return Lista de proveedores.
     */
    List<ProveedorResponseDTO> getAllProveedores();

    /**
     * Actualiza un proveedor existente.
     *
     * @param id El ID del proveedor a actualizar.
     * @param requestDTO Los nuevos datos para el proveedor.
     * @return El proveedor con los datos actualizados.
     * @throws com.beautycare.inventory.exception.ResourceNotFoundException Si no se encuentra.
     */
    ProveedorResponseDTO updateProveedor(Long id, ProveedorRequestDTO requestDTO);

    /**
     * Elimina un proveedor por su ID.
     *
     * @param id El ID del proveedor a eliminar.
     * @throws com.beautycare.inventory.exception.ResourceNotFoundException Si no se encuentra.
     */
    void deleteProveedor(Long id);
}