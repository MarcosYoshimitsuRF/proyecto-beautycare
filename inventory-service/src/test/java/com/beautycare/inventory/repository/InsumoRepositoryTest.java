package com.beautycare.inventory.repository;

import com.beautycare.inventory.model.Insumo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class InsumoRepositoryTest {

    @Autowired
    private InsumoRepository insumoRepository;

    private Insumo insumoGlobal;

    @BeforeEach
    void setUp() {
        // Preparamos un insumo de prueba antes de cada test
        insumoGlobal = new Insumo(null, "Shampoo Hidratante", 50, 10, "ml");
    }

    @Test
    @DisplayName("Test para guardar (Insertar) un insumo")
    void testGuardarInsumo() {
        // --- 1. Arrange (Preparar) ---
        // El insumoGlobal ya está listo desde el setUp

        // --- 2. Act (Actuar) ---
        Insumo insumoGuardado = insumoRepository.save(insumoGlobal);

        // --- 3. Assert (Verificar) ---
        assertThat(insumoGuardado).isNotNull();
        assertThat(insumoGuardado.getId()).isGreaterThan(0);
        assertThat(insumoGuardado.getNombre()).isEqualTo("Shampoo Hidratante");
        assertThat(insumoGuardado.getStock()).isEqualTo(50);
    }

    @Test
    @DisplayName("Test para listar insumos")
    void testListarInsumos() {
        // --- 1. Arrange (Preparar) ---
        Insumo insumo2 = new Insumo(null, "Tinte Rubio", 20, 5, "unidad");
        insumoRepository.save(insumoGlobal); // Insumo 1
        insumoRepository.save(insumo2);      // Insumo 2

        // --- 2. Act (Actuar) ---
        List<Insumo> listaInsumos = insumoRepository.findAll();

        // --- 3. Assert (Verificar) ---
        assertThat(listaInsumos).isNotNull();
        assertThat(listaInsumos.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Test para obtener un insumo por ID (Leer)")
    void testObtenerInsumoPorId() {
        // --- 1. Arrange (Preparar) ---
        Insumo insumoGuardado = insumoRepository.save(insumoGlobal);
        Long idGuardado = insumoGuardado.getId();

        // --- 2. Act (Actuar) ---
        Optional<Insumo> insumoEncontrado = insumoRepository.findById(idGuardado);

        // --- 3. Assert (Verificar) ---
        assertThat(insumoEncontrado).isPresent();
        assertThat(insumoEncontrado.get().getId()).isEqualTo(idGuardado);
    }

    @Test
    @DisplayName("Test para actualizar un insumo (ej. reponer stock)")
    void testActualizarInsumo() {
        // --- 1. Arrange (Preparar) ---
        Insumo insumoGuardado = insumoRepository.save(insumoGlobal);

        // --- 2. Act (Actuar) ---
        // Modificamos el objeto obtenido (simulamos reponer stock)
        insumoGuardado.setStock(100);
        insumoGuardado.setNombre("Shampoo Hidratante PRO");
        Insumo insumoActualizado = insumoRepository.save(insumoGuardado); // Save en un ID existente = UPDATE

        // --- 3. Assert (Verificar) ---
        assertThat(insumoActualizado).isNotNull();
        assertThat(insumoActualizado.getNombre()).isEqualTo("Shampoo Hidratante PRO");
        assertThat(insumoActualizado.getStock()).isEqualTo(100);
    }

    @Test
    @DisplayName("Test para eliminar un insumo")
    void testEliminarInsumo() {
        // --- 1. Arrange (Preparar) ---
        Insumo insumoGuardado = insumoRepository.save(insumoGlobal);
        Long idParaBorrar = insumoGuardado.getId();

        // --- 2. Act (Actuar) ---
        insumoRepository.deleteById(idParaBorrar);

        // --- 3. Assert (Verificar) ---
        Optional<Insumo> insumoBorrado = insumoRepository.findById(idParaBorrar);
        assertThat(insumoBorrado).isNotPresent(); // Verificamos que ya no existe
    }
}