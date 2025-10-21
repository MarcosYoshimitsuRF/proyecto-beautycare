package com.beautycare.api.service;

import com.beautycare.api.controller.dto.ClienteRequestDTO;
import com.beautycare.api.controller.dto.ClienteResponseDTO;

import java.util.List;

/**
 * Interfaz (contrato) para la lógica de negocio relacionada con Clientes.
 * Define las operaciones CRUD que el controlador podrá invocar.
 */
public interface ClienteService {

    /**
     * Crea un nuevo cliente en el sistema.
     *
     * @param clienteRequestDTO Datos del cliente a crear.
     * @return El cliente guardado con su ID asignado.
     */
    ClienteResponseDTO createCliente(ClienteRequestDTO clienteRequestDTO);

    /**
     * Obtiene un cliente por su ID.
     *
     * @param id El ID del cliente a buscar.
     * @return El cliente encontrado.
     * @throws RuntimeException (o una excepción personalizada) si no se encuentra.
     */
    ClienteResponseDTO getClienteById(Long id);

    /**
     * Devuelve una lista de todos los clientes.
     *
     * @return Lista de clientes.
     */
    List<ClienteResponseDTO> getAllClientes();

    /**
     * Actualiza un cliente existente.
     *
     * @param id El ID del cliente a actualizar.
     * @param clienteRequestDTO Los nuevos datos para el cliente.
     * @return El cliente con los datos actualizados.
     * @throws RuntimeException (o una excepción personalizada) si no se encuentra.
     */
    ClienteResponseDTO updateCliente(Long id, ClienteRequestDTO clienteRequestDTO);

    /**
     * Elimina un cliente por su ID.
     *
     * @param id El ID del cliente a eliminar.
     * @throws RuntimeException (o una excepción personalizada) si no se encuentra.
     */
    void deleteCliente(Long id);
}