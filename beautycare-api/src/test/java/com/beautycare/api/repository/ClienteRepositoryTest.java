package com.beautycare.api.repository;

import com.beautycare.api.model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ClienteRepositoryTest {

    @Autowired
    private ClienteRepository clienteRepository;

    private Cliente clienteGlobal;

    @BeforeEach
    void setUp() {
        // Preparamos un cliente de prueba antes de cada test
        clienteGlobal = new Cliente(null, "Cliente de Prueba", "999888777", "prueba@test.com");
    }

    @Test
    @DisplayName("Test para guardar (Insertar) un cliente")
    void testGuardarCliente() {
        // --- 1. Arrange (Preparar) ---
        // El clienteGlobal ya está listo desde el setUp

        // --- 2. Act (Actuar) ---
        Cliente clienteGuardado = clienteRepository.save(clienteGlobal);

        // --- 3. Assert (Verificar) ---
        assertThat(clienteGuardado).isNotNull();
        assertThat(clienteGuardado.getId()).isGreaterThan(0);
        assertThat(clienteGuardado.getNombre()).isEqualTo("Cliente de Prueba");
    }

    @Test
    @DisplayName("Test para listar clientes")
    void testListarClientes() {
        // --- 1. Arrange (Preparar) ---
        Cliente cliente2 = new Cliente(null, "Cliente Dos", "111222333", "dos@test.com");
        clienteRepository.save(clienteGlobal); // Cliente 1
        clienteRepository.save(cliente2);      // Cliente 2

        // --- 2. Act (Actuar) ---
        List<Cliente> listaClientes = clienteRepository.findAll();

        // --- 3. Assert (Verificar) ---
        assertThat(listaClientes).isNotNull();
        assertThat(listaClientes.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Test para obtener un cliente por ID (Leer)")
    void testObtenerClientePorId() {
        // --- 1. Arrange (Preparar) ---
        Cliente clienteGuardado = clienteRepository.save(clienteGlobal);
        Long idGuardado = clienteGuardado.getId();

        // --- 2. Act (Actuar) ---
        Optional<Cliente> clienteEncontrado = clienteRepository.findById(idGuardado);

        // --- 3. Assert (Verificar) ---
        assertThat(clienteEncontrado).isPresent();
        assertThat(clienteEncontrado.get().getId()).isEqualTo(idGuardado);
    }

    @Test
    @DisplayName("Test para actualizar un cliente")
    void testActualizarCliente() {
        // --- 1. Arrange (Preparar) ---
        Cliente clienteGuardado = clienteRepository.save(clienteGlobal);

        // --- 2. Act (Actuar) ---
        // Modificamos el objeto obtenido
        clienteGuardado.setNombre("Cliente Actualizado");
        clienteGuardado.setEmail("nuevo@email.com");
        Cliente clienteActualizado = clienteRepository.save(clienteGuardado); // Save en un ID existente = UPDATE

        // --- 3. Assert (Verificar) ---
        assertThat(clienteActualizado).isNotNull();
        assertThat(clienteActualizado.getNombre()).isEqualTo("Cliente Actualizado");
        assertThat(clienteActualizado.getEmail()).isEqualTo("nuevo@email.com");
    }

    @Test
    @DisplayName("Test para eliminar un cliente")
    void testEliminarCliente() {
        // --- 1. Arrange (Preparar) ---
        Cliente clienteGuardado = clienteRepository.save(clienteGlobal);
        Long idParaBorrar = clienteGuardado.getId();

        // --- 2. Act (Actuar) ---
        clienteRepository.deleteById(idParaBorrar);

        // --- 3. Assert (Verificar) ---
        Optional<Cliente> clienteBorrado = clienteRepository.findById(idParaBorrar);
        assertThat(clienteBorrado).isNotPresent(); // Verificamos que ya no existe
    }
}