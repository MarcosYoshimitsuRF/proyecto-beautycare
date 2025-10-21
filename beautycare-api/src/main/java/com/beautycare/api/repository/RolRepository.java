package com.beautycare.api.repository;

import com.beautycare.api.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    // JpaRepository provee métodos como:
    // save(), findById(), findAll(), deleteById(), etc.
}