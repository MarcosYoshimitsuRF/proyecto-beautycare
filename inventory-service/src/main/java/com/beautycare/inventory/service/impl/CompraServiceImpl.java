package com.beautycare.inventory.service.impl;

import com.beautycare.inventory.controller.dto.CompraRequestDTO;
import com.beautycare.inventory.controller.dto.CompraResponseDTO;
import com.beautycare.inventory.exception.ResourceNotFoundException;
import com.beautycare.inventory.model.Compra;
import com.beautycare.inventory.model.Proveedor;
import com.beautycare.inventory.repository.CompraRepository;
import com.beautycare.inventory.repository.ProveedorRepository;
import com.beautycare.inventory.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompraServiceImpl implements CompraService {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private ProveedorRepository proveedorRepository; // Necesario para buscar el proveedor

    @Override
    @Transactional
    public CompraResponseDTO createCompra(CompraRequestDTO requestDTO) {
        // 1. Mapear DTO (Request) a Entidad
        Compra compra = new Compra();
        compra.setFecha(requestDTO.getFecha());
        compra.setTotal(requestDTO.getTotal());

        // 2. Buscar y asignar el proveedor (si se proporcionó ID)
        if (requestDTO.getProveedorId() != null) {
            Proveedor proveedor = proveedorRepository.findById(requestDTO.getProveedorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con ID: " + requestDTO.getProveedorId()));
            compra.setProveedor(proveedor);
        }

        // 3. Guardar en la BD
        Compra compraGuardada = compraRepository.save(compra);

        // (NOTA: Aquí iría la lógica futura para reponer stock basado en detalles de la compra)

        // 4. Mapear Entidad a DTO (Response) y devolver
        return new CompraResponseDTO(compraGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public CompraResponseDTO getCompraById(Long id) {
        // 1. Buscar o lanzar excepción
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compra no encontrada con ID: " + id));

        // 2. Mapear a DTO (Response) y devolver
        return new CompraResponseDTO(compra);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompraResponseDTO> getAllCompras() {
        // 1. Buscar todas
        List<Compra> compras = compraRepository.findAll();

        // 2. Mapear la lista de Entidades a DTOs (Response)
        return compras.stream()
                .map(CompraResponseDTO::new) // Usa el constructor del DTO
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CompraResponseDTO updateCompra(Long id, CompraRequestDTO requestDTO) {
        // 1. Buscar la compra a actualizar o lanzar excepción
        Compra compraExistente = compraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compra no encontrada con ID: " + id));

        // 2. Actualizar los campos
        compraExistente.setFecha(requestDTO.getFecha());
        compraExistente.setTotal(requestDTO.getTotal());

        // 3. Actualizar el proveedor (si se proporcionó ID o si se quiere desasignar)
        if (requestDTO.getProveedorId() != null) {
            Proveedor proveedor = proveedorRepository.findById(requestDTO.getProveedorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con ID: " + requestDTO.getProveedorId()));
            compraExistente.setProveedor(proveedor);
        } else {
            compraExistente.setProveedor(null); // Permite desasignar proveedor
        }

        // 4. Guardar los cambios (JPA detecta UPDATE)
        Compra compraActualizada = compraRepository.save(compraExistente);

        // (NOTA: Considerar si las compras deben ser actualizables y si afecta al stock)

        // 5. Mapear a DTO (Response) y devolver
        return new CompraResponseDTO(compraActualizada);
    }

    @Override
    @Transactional
    public void deleteCompra(Long id) {
        // 1. Verificar si existe
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compra no encontrada con ID: " + id));

        // (NOTA: Considerar si eliminar una compra debe revertir el stock)

        // 2. Eliminar
        compraRepository.delete(compra);
    }
}