package com.beautycare.api.repository;

import com.beautycare.api.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Debe estar vacío aquí (a menos que añadas métodos personalizados después)
}